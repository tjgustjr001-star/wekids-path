package com.spring.dao.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminStatsDAOImpl implements AdminStatsDAO {

    @Autowired
    private SqlSession session;

    @Override
    public Map<String, Object> selectDashboardOverview() throws SQLException {
        return session.selectOne("AdminStats-Mapper.selectDashboardOverview");
    }

    @Override
    public List<Map<String, Object>> selectUserTypeList() throws SQLException {
        return session.selectList("AdminStats-Mapper.selectUserTypeList");
    }

    @Override
    public List<Map<String, Object>> selectUserGrowthList(String period) throws SQLException {
        return session.selectList("AdminStats-Mapper.selectUserGrowthList", period);
    }

    @Override
    public List<Map<String, Object>> selectClassStatsList() throws SQLException {
        return session.selectList("AdminStats-Mapper.selectClassStatsList");
    }

    @Override
    public Map<String, Object> selectClassDetailSummary(int classId) throws SQLException {
        return session.selectOne("AdminStats-Mapper.selectClassDetailSummary", classId);
    }

    @Override
    public List<Map<String, Object>> selectSubmissionTrendList(int classId) throws SQLException {
        return session.selectList("AdminStats-Mapper.selectSubmissionTrendList", classId);
    }

    @Override
    public List<Map<String, Object>> selectClassLearnStatusList(int classId) throws SQLException {
        return session.selectList("AdminStats-Mapper.selectClassLearnStatusList", classId);
    }

    @Override
    public List<Map<String, Object>> selectStudentStatsList(int classId) throws SQLException {
        return session.selectList("AdminStats-Mapper.selectStudentStatsList", classId);
    }
}