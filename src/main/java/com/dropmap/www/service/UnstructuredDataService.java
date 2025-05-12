package com.dropmap.www.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UnstructuredDataService {
    public Set<String> getUnstructuredDataCoords() {
        Set<String> unstructuredData = new HashSet<String>();
        return unstructuredData;
    }
}
