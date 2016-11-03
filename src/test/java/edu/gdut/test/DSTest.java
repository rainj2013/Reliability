package edu.gdut.test;

import edu.gdut.service.DS.EvidenceDS;
import org.junit.Test;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-4
 */
public class DSTest {
    @Test
    public void test(){
        EvidenceDS  ds0 = new EvidenceDS(0.5,0.3);
        EvidenceDS  ds1 = new EvidenceDS(0.6,0.3);
        System.out.println(ds0.add(ds1));
    }
}
