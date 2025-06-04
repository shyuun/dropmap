package com.dropmap.www.web;

import com.dropmap.www.dto.DistrictInfoResponse;
import com.dropmap.www.dto.GeolocationInfoResponse;
import com.dropmap.www.service.DropmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DropmapController {

    private final DropmapService dropmapService;

    @GetMapping("/api/getDistrictInfo")
    public List<DistrictInfoResponse> getDistrictInfo(@RequestParam int zoomLevel, @RequestParam double lat1, @RequestParam double lat2, @RequestParam double lot1, @RequestParam double lot2) throws IOException {
        return dropmapService.getDistrictInfo(zoomLevel,lat1,lat2,lot1,lot2);
    }

    @GetMapping("/api/getMarkerInfo")
    public List<GeolocationInfoResponse> getMarkerInfo(@RequestParam double lat1, @RequestParam double lat2, @RequestParam double lot1, @RequestParam double lot2) throws IOException {
        return dropmapService.getMarkerInfo(lat1,lat2,lot1,lot2);
    }
}
