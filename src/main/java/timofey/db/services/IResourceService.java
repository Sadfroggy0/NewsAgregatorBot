package timofey.db.services;

import timofey.entities.Resource;

import java.util.List;

public interface IResourceService {

    List<Resource> findAll();
    Resource findById(Long id);
    List<Resource> findUserResources(Long userId);
    Resource findByName(String name);
    Resource findByUrl (String url);
    Resource save(Resource resource);
    List<Resource> saveAll(List<Resource> resourceList);
}
