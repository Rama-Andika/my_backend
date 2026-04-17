package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.children.data.ChildrenDTO;
import com.oxysystem.general.dto.general.parent.data.ParentDTO;
import com.oxysystem.general.mapper.transaction.SalesMapper;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.model.tenant.general.PlaygroundChildren;
import com.oxysystem.general.model.tenant.general.PlaygroundParent;
import com.oxysystem.general.model.tenant.general.PlaygroundRegistration;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.posmaster.PriceType;
import com.oxysystem.general.model.tenant.transaction.PlaygroundSession;
import com.oxysystem.general.model.tenant.transaction.SalesTaking;
import com.oxysystem.general.repository.tenant.general.PlaygroundParentRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.admin.UserService;
import com.oxysystem.general.service.general.PlaygroundChildrenService;
import com.oxysystem.general.service.general.PlaygroundParentService;
import com.oxysystem.general.service.general.PlaygroundRegistrationService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.transaction.PlaygroundSessionService;
import com.oxysystem.general.service.transaction.SalesTakingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlaygroundParentServiceImpl implements PlaygroundParentService {
    private final PlaygroundParentRepository playgroundParentRepository;
    private final PlaygroundRegistrationService  playgroundRegistrationService;
    private final UserService userService;
    private final PlaygroundChildrenService playgroundChildrenService;
    private final PlaygroundSessionService playgroundSessionService;
    private final SalesTakingService salesTakingService;
    private final ItemMasterService  itemMasterService;
    private final SalesMapper salesMapper;

    public PlaygroundParentServiceImpl(PlaygroundParentRepository playgroundParentRepository, PlaygroundRegistrationService playgroundRegistrationService, UserService userService, PlaygroundChildrenService playgroundChildrenService, PlaygroundSessionService playgroundSessionService, SalesTakingService salesTakingService, ItemMasterService itemMasterService, SalesMapper salesMapper) {
        this.playgroundParentRepository = playgroundParentRepository;
        this.playgroundRegistrationService = playgroundRegistrationService;
        this.userService = userService;
        this.playgroundChildrenService = playgroundChildrenService;
        this.playgroundSessionService = playgroundSessionService;
        this.salesTakingService = salesTakingService;
        this.itemMasterService = itemMasterService;
        this.salesMapper = salesMapper;
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<?> save(ParentDTO body) {
        PlaygroundParent playgroundParent = new PlaygroundParent();
        playgroundParent.setName(body.getName());
        playgroundParent.setPhone(body.getPhone());
        playgroundParent.setCode(body.getCode());

        User user = userService.findById(Long.valueOf(body.getUserId())).orElse(null);
        playgroundParent = playgroundParentRepository.save(playgroundParent);

        //Insert to playground registration
        PlaygroundRegistration playgroundRegistration = new PlaygroundRegistration();
        playgroundRegistration.setParent(playgroundParent);
        playgroundRegistration = playgroundRegistrationService.save(playgroundRegistration);

        //This map for response
        Map<String, Object> responseMap = new HashMap<>();

        //This bunch of code is used to get list of items in single query
        Set<Long> itemIds = body.getChildren().stream().map(c -> Long.valueOf(c.getItemId())).collect(Collectors.toSet());
        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new  ArrayList<>(itemIds));
        Map<Long, ItemMaster> itemMasterMap = itemMasters.stream().collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity()));

        if(body.getChildren() != null && !body.getChildren().isEmpty()) {
            List<PlaygroundChildren> children = new ArrayList<>();

            for(ChildrenDTO c:  body.getChildren()) {
                PlaygroundChildren child = new PlaygroundChildren();
                child.setPlaygroundParent(playgroundParent);
                child.setName(c.getName());
                child.setAge(c.getAge());

                child = playgroundChildrenService.save(child);
                children.add(child);

                //Insert playground session
                PlaygroundSession playgroundSession = new PlaygroundSession();
                playgroundSession.setPlaygroundChildren(child);
                playgroundSession.setStartTime(LocalDateTime.now());

                LocalDateTime endTime =  LocalDateTime.now();

                //Search minute playing from price type item
                ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(c.getItemId()));
                if(itemMaster != null){
                    PriceType priceType = itemMaster.getPriceTypes().stream().min(Comparator.comparing(PriceType::getConvQty, Comparator.nullsLast(Double::compareTo))
                            .thenComparing(PriceType::getQtyFrom, Comparator.nullsLast(Integer::compareTo))).orElse(null);

                    if(priceType != null){
                         endTime = playgroundSession.getStartTime().plusMinutes(priceType.getConvQty().longValue());
                    }
                }

                playgroundSession.setEndTime(endTime);
                playgroundSession.setIsActive(1);
                playgroundSession.setUser(user);
                playgroundSession.setCode(c.getCode());
                playgroundSessionService.save(playgroundSession);
            }
            playgroundParent.setChildren(children);
        }

        //Insert to sales taking
        SalesTaking salesTaking = salesTakingService.saveForPlayground(playgroundRegistration, itemMasterMap, body.getChildren(), user);

        responseMap.put("reg_number", playgroundRegistration.getNumber());
        responseMap.put("parent_name", playgroundParent.getName());
        responseMap.put("parent_phone", playgroundParent.getPhone());
        responseMap.put("children", playgroundParent.getChildren());
        SuccessResponse<?> response = new SuccessResponse<>("success", responseMap);
        return ResponseEntity.ok(response);
    }
}
