package ru.chuchalov.messaging;

import java.util.Collection;

public interface PHMessageCommonRepository<T> {
	public T save(T domain);
	public Iterable<T> save(Collection<T> domains);
	public void delete(T domain);
	public T findById(String id);
	public Iterable<T> findAll();
}
