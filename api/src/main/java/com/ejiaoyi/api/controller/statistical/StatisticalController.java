package com.ejiaoyi.api.controller.statistical;

import com.ejiaoyi.api.controller.BaseController;
import com.ejiaoyi.api.dto.*;
import com.ejiaoyi.api.service.impl.StatisticalDataServiceImpl;
import com.ejiaoyi.common.annotation.ApiAuthentication;
import com.ejiaoyi.common.dto.statistical.StatisticalDataDTO;
import com.ejiaoyi.common.service.impl.ApiAuthServiceImpl;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据统计对接接口
 *
 * @author Mike
 * @since 2021-03-23
 */
@Api(value = "数据统计对接接口", tags = "数据统计对接接口")
@RestController
@RequestMapping("/statisticalData")
public class StatisticalController implements BaseController {

    private static final String API_NAME = "statisticalData";

    @Autowired
    private StatisticalDataServiceImpl statisticalDataService;

    @Override
    @PostMapping("/getToken")
    @ApiOperation(value = "获取TOKEN", notes = "获取TOKEN",response = String.class)
    @ApiOperationSupport(
            order = 1,
            author = "Mike"
    )
    //该注解是用于验证授权值是否有效,token是否有效
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public String getToken(@RequestBody @Valid GetToken getToken) {
        return statisticalDataService.getToken(getToken);
    }

    @PostMapping("/getProjectData")
    @ApiOperation(value = "获取项目数据", notes = "获取项目数据",response = ProjectDataDTO.class)
    @ApiOperationSupport(
            order = 2,
            author = "Mike"
    )
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public List<ProjectDataDTO> getProjectData(@RequestBody @Valid GetProjectDataParam getProjectDataParam) {
        return statisticalDataService.listProjectData(getProjectDataParam);
    }

    @PostMapping("/getStatisticalInfo")
    @ApiOperation(value = "统计项目情况", notes = "统计项目情况",response = StatisticalDataDTO.class)
    @ApiOperationSupport(
            order = 3,
            author = "Mike"
    )
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public StatisticalDataDTO getStatisticalInfo(@RequestBody @Valid GetStatisticalDataParam getStatisticalDataParam) {
        return statisticalDataService.getStatisticalInfo(getStatisticalDataParam);
    }
}
