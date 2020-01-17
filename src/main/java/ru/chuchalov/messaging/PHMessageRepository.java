package ru.chuchalov.messaging;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

@Repository
public class PHMessageRepository implements PHMessageCommonRepository<PHMessage> {
	
	private Map<String, PHMessage> phMessages = new HashMap<>();
	
	public PHMessage save(PHMessage domain) {
		PHMessage result = phMessages.get(domain.getId());
		if(result != null) {
			result.setMessage(domain.getMessage());
			result.setError(domain.isError());
			result.setValid(domain.isValid());
		}
		phMessages.put(domain.getId(), domain);
		return phMessages.get(domain.getId());
	}
	
    public Iterable<PHMessage> save(Collection<PHMessage> domains) {
        domains.forEach(this::save);
        return findAll();
    }
	
    public void delete(PHMessage domain) {
        phMessages.remove(domain.getId());
    }
	
    public PHMessage findById(String id) {
        return phMessages.get(id);
    }
    
    public Iterable<PHMessage> findAll() {
        return phMessages.entrySet().stream().sorted(entryComparator).
        		map(Map.Entry::getValue).collect(Collectors.toList());
    }
    
    private Comparator<Map.Entry<String,PHMessage>> entryComparator = 
    		(Map.Entry<String, PHMessage> o1, Map.Entry<String, PHMessage> o2) -> { 
    			return o1.getValue().getMessage().compareTo(o2.getValue().getMessage());
    };

}
