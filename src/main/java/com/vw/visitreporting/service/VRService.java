package com.vw.visitreporting.service;

import java.io.Serializable;
import java.util.List;


public interface VRService<T> {

	T getById(Serializable id);
	T save(T t);
	String delete(T t);
	List<T> list();
}

