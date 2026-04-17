package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.grab.view.ListMartCategoryDTO;
import com.oxysystem.general.dto.posmaster.grabMartGroup.data.GrabMartItemGroupDTO;
import com.oxysystem.general.dto.posmaster.grabMartGroup.view.GrabMartItemGroupViewDTO;
import com.oxysystem.general.dto.posmaster.grabMartGroup.view.GrabmartItemGroupForSelectDTO;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.posmaster.GrabmartItemCategory;
import com.oxysystem.general.model.tenant.posmaster.GrabmartItemGroup;
import com.oxysystem.general.repository.tenant.posmaster.GrabmartItemGroupRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.response.grab.GrabFailedResponse;
import com.oxysystem.general.service.posmaster.GrabmartItemCategoryService;
import com.oxysystem.general.service.posmaster.GrabmartItemGroupService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GrabmartItemGroupServiceImpl implements GrabmartItemGroupService {
    @Value("${grabmart.endpoint.list-mart-categories}")
    private String endpointListMartCategories;
    private final WebClient grabMartClient;
    private final GrabmartItemGroupRepository grabmartItemGroupRepository;
    private final GrabmartItemCategoryService grabmartItemCategoryService;

    public GrabmartItemGroupServiceImpl(WebClient grabMartClient, GrabmartItemGroupRepository grabmartItemGroupRepository, GrabmartItemCategoryService grabmartItemCategoryService) {
        this.grabMartClient = grabMartClient;
        this.grabmartItemGroupRepository = grabmartItemGroupRepository;
        this.grabmartItemCategoryService = grabmartItemCategoryService;
    }


    @Override
    public ResponseEntity<?> update(Long id, GrabMartItemGroupDTO body) {
        GrabmartItemGroup grabmartItemGroup = grabmartItemGroupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("grab category not found!"));
        grabmartItemGroup.setSequence(body.getSequence());
        grabmartItemGroupRepository.save(grabmartItemGroup);

        SuccessResponse<String> response = new SuccessResponse<>("success", String.valueOf(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getGrabMartItemGroups(String name) {
        SuccessResponse<List<GrabMartItemGroupViewDTO>> response = new SuccessResponse<>("success", grabmartItemGroupRepository.getGrabMartItemGroups(name));
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<?> syncCategories(String token, String countryCode) {
        if(countryCode == null || countryCode.isEmpty()) throw new ResourceNotFoundException("country code cannot be empty!");

        ListMartCategoryDTO categories = grabMartClient.get()
                .uri(endpointListMartCategories + "?countryCode=" + countryCode)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(GrabFailedResponse.class)
                        .switchIfEmpty(Mono.error(new GrabException("Get list mart category error", "unauthorized")))
                        .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage())))
                )
                .bodyToMono(ListMartCategoryDTO.class)
                .block();

        if(categories != null){
            List<GrabmartItemCategory> grabmartItemCategories = new ArrayList<>();

            categories.getCategories()
                    .forEach(category -> {
                        GrabmartItemGroup itemGroupMapping = grabmartItemGroupRepository.findByGrabmartId(category.getId())
                                .map(itemGroup -> {
                                    itemGroup.setName(category.getName());
                                    return itemGroup;
                                })
                                .orElse(new GrabmartItemGroup(category.getName(), category.getId()));
                        itemGroupMapping = grabmartItemGroupRepository.save(itemGroupMapping);

                        GrabmartItemGroup finalItemGroupMapping = itemGroupMapping;
                        category.getSubCategories()
                                .forEach(subCategory -> {
                                    GrabmartItemCategory itemCategoryMapping = grabmartItemCategoryService.findByGrabmartSubCategoryId(subCategory.getId())
                                            .map(itemCategory -> {
                                                itemCategory.setName(subCategory.getName());
                                                return itemCategory;
                                            })
                                            .orElse(new GrabmartItemCategory(subCategory.getName(), subCategory.getId(), finalItemGroupMapping));
                                    grabmartItemCategories.add(itemCategoryMapping);
                                });
                    });

            if(!grabmartItemCategories.isEmpty()) grabmartItemCategoryService.saveAll(grabmartItemCategories);
        }

        SuccessResponse<String> response = new SuccessResponse<>("success", null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getGrabmartItemGroupForSelectForApi() {
        List<GrabmartItemGroup> itemGroups = grabmartItemGroupRepository.findAll();
        itemGroups.sort(Comparator.comparing(GrabmartItemGroup::getName));
        List<GrabmartItemGroupForSelectDTO> itemGroupsForSelect = new ArrayList<>();

        for(GrabmartItemGroup grabmartItemGroup: itemGroups){
            GrabmartItemGroupForSelectDTO content = new GrabmartItemGroupForSelectDTO();
            content.setId(grabmartItemGroup.getId().toString());
            content.setName(grabmartItemGroup.getName());

            List<GrabmartItemGroupForSelectDTO.GrabmartItemCategoryForSelectDTO> subCategories = new ArrayList<>();
            for(GrabmartItemCategory grabmartItemCategory: grabmartItemGroup.getGrabmartItemCategories()){
                GrabmartItemGroupForSelectDTO.GrabmartItemCategoryForSelectDTO subCategory = new GrabmartItemGroupForSelectDTO.GrabmartItemCategoryForSelectDTO();
                subCategory.setId(grabmartItemCategory.getId().toString());
                subCategory.setName(grabmartItemCategory.getName());
                subCategories.add(subCategory);
            }
            content.setSubCategories(subCategories);
            itemGroupsForSelect.add(content);
        }
        SuccessResponse<List<GrabmartItemGroupForSelectDTO>> response = new SuccessResponse<>("success",itemGroupsForSelect);
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<GrabmartItemGroup> findById(Long id) {
        return grabmartItemGroupRepository.findById(id);
    }

    @Override
    public List<GrabmartItemGroup> findAllGrabMartItemGroupByIds(List<Long> ids) {
        return grabmartItemGroupRepository.findAllGrabMartItemGroupByIds(ids);
    }

    @Override
    public ResponseEntity<?> toCsv(String name) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                "name","sequence"
        ));


        List<GrabMartItemGroupViewDTO> grabmartItemGroups = grabmartItemGroupRepository.getGrabMartItemGroups(name);

        for(GrabMartItemGroupViewDTO data: grabmartItemGroups){
            csvPrinter.printRecord(
                    data.getName(),
                    data.getSequence()
            );
        }

        csvPrinter.flush();

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=grabmart-category.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
