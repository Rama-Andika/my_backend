package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.model.tenant.general.PlaygroundChildren;
import com.oxysystem.general.repository.tenant.general.PlaygroundChildrenRepository;
import com.oxysystem.general.service.general.PlaygroundChildrenService;
import org.springframework.stereotype.Service;

@Service
public class PlaygroundChildrenServiceImpl implements PlaygroundChildrenService {
    private final PlaygroundChildrenRepository playgroundChildrenRepository;

    public PlaygroundChildrenServiceImpl(PlaygroundChildrenRepository playgroundChildrenRepository) {
        this.playgroundChildrenRepository = playgroundChildrenRepository;
    }

    @Override
    public PlaygroundChildren save(PlaygroundChildren playgroundChildren) {
        return playgroundChildrenRepository.save(playgroundChildren);
    }
}
