package com.dropmap.www.web;

import com.dropmap.www.dto.DistrictInfoResponse;
import com.dropmap.www.service.DropmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
    public List<DistrictInfoResponse> getDistrictInfo(@RequestParam int zoomLevel) throws IOException {
        return dropmapService.getDistrictInfo(zoomLevel);
    }
}
