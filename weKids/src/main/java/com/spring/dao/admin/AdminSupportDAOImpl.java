package com.spring.dao.admin;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.spring.dto.SupportVO;

@Repository
public class AdminSupportDAOImpl implements AdminSupportDAO {

    @Autowired
    private SqlSession sqlSession;

    // Mapper의 namespace와 id를 조합한 경로입니다.
    // 기존 Support-Mapper.xml의 namespace가 "com.spring.dao.SupportDAO"로 되어 있으므로 
    // 관리자용 쿼리를 해당 Mapper에 추가하거나, 별도의 Admin-Mapper를 만드실 때 이 경로를 맞춰주세요.
    private static final String NAMESPACE = "com.spring.dao.SupportDAO";

    @Override
    public List<SupportVO> selectAllSupports() {
        // 모든 문의 내역을 리스트로 반환합니다.
        return sqlSession.selectList(NAMESPACE + ".selectAllSupports");
    }
}