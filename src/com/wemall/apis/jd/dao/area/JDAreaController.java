package com.wemall.apis.jd.dao.area;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;

@Controller
public class JDAreaController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource(name = "jDAreaDao")
    private JDArea jDAreaDao;

    protected static Logger logger = Logger.getLogger(JDAreaController.class);


    @RequestMapping("/province.htm")
    @ResponseBody
    public String province() {
        int i = 0;
        List<String> list = jDAreaDao.getAllOneArea();
        for (String p : list) {
            String cname = p.split(":")[0].toString().substring(0, 2);
            String cid = p.split(":")[1].toString();
            jdbcTemplate.update("INSERT INTO wemall_area (id,addTime, deleteStatus, areaName, level, sequence,common) " +
                    "VALUE ('" + i + "','2017-07-30 15:38:03',b'0','\"+tname+\"',0,0,b'0')");
            i++;
        }
        return "success :" + String.valueOf(i);
    }


    @RequestMapping("/town.htm")
    @ResponseBody
    public String town(String oid) {
        List<String> onelist = jDAreaDao.getAllTwoAreaByParentId(oid);
        for (String p : onelist) {
            String tid = p.split(":")[1].toString();
            List<String> threelist = jDAreaDao.getAllThreeAreaByParentId(tid);
            int id=jdbcTemplate.queryForInt("select id from wemall_area where areaName='"+p.split(":")[0]+"'");
            for (String c : threelist) {
                String cname = c.split(":")[0].toString();
                String cid = c.split(":")[1].toString();

                int parentid = 0;
                try {
                    parentid = jdbcTemplate.queryForInt("select id from wemall_area where areaName = '" + cname + "' and level=2 and parent_id='"+id+"'");
                } catch (DataAccessException e) {
                    System.out.println("error:select id from wemall_area where areaName = '" + cname + "' and level=2 and parent_id='"+id+"'");
                    continue;
                }
                List<String> fourlist = jDAreaDao.getAllFourAreaByParentId(cid);
                if (fourlist != null) {
                    for (String town : fourlist) {
                        String tname = town.split(":")[0].toString();
                        int count = jdbcTemplate.queryForInt("select count(*) from wemall_area where parent_id ='" + parentid + "' and level=3 and areaName='"+tname+"' ");
                        if (count == 0) {
                            jdbcTemplate.update("INSERT into wemall_area (addTime, deleteStatus, areaName, level, sequence, parent_id,common)" +
                                    "  VALUE ('2013-07-30 15:38:03',b'0','" + tname + "',3,0,'" + parentid + "',b'0')");
                            System.out.println("INSERT into wemall_area (addTime, deleteStatus, areaName, level, sequence, parent_id,common)" +
                                    "  VALUE ('2013-07-30 15:38:03',b'0','" + tname + "',3,0,'" + parentid + "',b'0')");
                        }

                    }
                }

            }
        }
        return "success!";
    }
}
