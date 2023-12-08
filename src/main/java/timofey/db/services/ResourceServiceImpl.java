package timofey.db.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import timofey.db.repositories.ResourceRepository;
import timofey.entities.Resource;

import java.util.List;

@Service
public class ResourceServiceImpl implements IResourceService{

    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public Resource findById(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Resource> findUserResources(Long userId) {
        return resourceRepository.findResourcesByUserId(userId);
    }

    @Override
    public Resource findByName(String name) {
        return resourceRepository.findByNameContainingIgnoreCase(name).orElse(null);
    }

    @Override
    public Resource findByUrl(String url) {
        return resourceRepository.findByUrl(url).orElse(null);
    }

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public List<Resource> saveAll(List<Resource> resourceList) {
        return resourceRepository.saveAll(resourceList);
    }
}
