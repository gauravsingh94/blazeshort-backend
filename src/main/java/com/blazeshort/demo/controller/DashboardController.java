package com.blazeshort.demo.controller;

import com.blazeshort.demo.model.dto.DashboardAnalyticsResponse;
import com.blazeshort.demo.model.dto.OverallDashboardAnalyticsResponse;
import com.blazeshort.demo.service.DashboardService;
import com.blazeshort.demo.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/analytics/{code}")
    public DashboardAnalyticsResponse analytics(@PathVariable String code) {

        Long userId = SecurityUtils.getCurrentUserId();
        return dashboardService.getDashboardAnalytics(code, userId);
    }

    @GetMapping("/overall")
    public OverallDashboardAnalyticsResponse overallAnalytics() {
        Long userId = SecurityUtils.getCurrentUserId();
        return dashboardService.getOverallDashboardAnalytics(userId);
    }
}
