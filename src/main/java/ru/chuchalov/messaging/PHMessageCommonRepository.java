package ru.chuchalov.messaging;

import java.util.Collection;

/**
 * Интерфейс определяет набор методов обработки (юз-кейсы) основной бизнес-сущности {@link PHMessage}
 * @author Andrei Chuchalov
 *
 * @param <T> определяет сущность для работы с методами
 */
public interface PHMessageCommonRepository<T> {
	
	/**
	 * Метод сохранения текущей сущности
	 * @param domain измененная сущность
	 * @return сохраненная сущность
	 */
	public T save(T domain);
	
	/**
	 * Метод сохраняет набор измененных сущностей
	 * @param domains набор изменных сущностей
	 * @return список сохраненных сущностей
	 */
	public Iterable<T> save(Collection<T> domains);
	
	/**
	 * Метод удаляет текущую сущность
	 * @param domain сущность для удаления
	 */
	public void delete(T domain);
	
	/**
	 * Метод осуществляет поиск сущности по идентификатору
	 * @param id идентификатор сущности
	 * @return найденную сущность
	 */
	public T findById(String id);
	
	/**
	 * Метод осуществляет поиск всех сущностей в списке
	 * @return набор сущностей
	 */
	public Iterable<T> findAll();
}
