package ru.chuchalov.messaging;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

/**
 * Класс реализует методы обработки сущностей {@link PHMessage} - usecases
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 *
 */
@Repository
public class PHMessageRepository implements PHMessageCommonRepository<PHMessage> {
	
	private Map<String, PHMessage> phMessages = new HashMap<>();
	
	/**
	 * Метод сохранения сущности
	 * @param domain сущность {@link PHMessage}
	 * @return сохраненная сущность {@link PHMessage}
	 */
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
	
	/**
	 * Метод сохранения сущностей списком
	 * @param domains набор измененных сущностей {@link PHMessage}
	 * @return набор сохраненных сущностей {@link PHMessage}
	 */
    public Iterable<PHMessage> save(Collection<PHMessage> domains) {
        domains.forEach(this::save);
        return findAll();
    }
	
    /**
     * Метод удаляет сущность {@link PHMessage}
     * @param сущность {@link PHMessage}
     */
    public void delete(PHMessage domain) {
        phMessages.remove(domain.getId());
    }
	
    /**
     * Метод поиска сущности по идентификатору
     * @param id идентификатор сущности
     * @return сущность {@link PHMessage}
     */
    public PHMessage findById(String id) {
        return phMessages.get(id);
    }
    
    /** 
     * Метод возвращает все сущности списка, отсортированные по сообщениям по алфавиту
     * @return набор сущностей {@link PHMessage}
     */
    public Iterable<PHMessage> findAll() {
        return phMessages.entrySet().stream().sorted(entryComparator).
        		map(Map.Entry::getValue).collect(Collectors.toList());
    }
    
    /**
     * Компаратор сравнивает сообщения по алфавиту
     */
    private Comparator<Map.Entry<String,PHMessage>> entryComparator = 
    		(Map.Entry<String, PHMessage> o1, Map.Entry<String, PHMessage> o2) -> { 
    			return o1.getValue().getMessage().compareTo(o2.getValue().getMessage());
    };

}
