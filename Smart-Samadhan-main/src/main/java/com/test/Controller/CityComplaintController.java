package com.test.Controller;

import com.test.dao.CityComplaintDAO;
import com.test.model.CityComplaintModel;

import java.util.List;

public class CityComplaintController {
    private CityComplaintDAO dao = new CityComplaintDAO();

    public List<CityComplaintModel> getCityComplaintData() {
        return dao.getCityComplaintCounts();
    }
}
