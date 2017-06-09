/**   
* @Package com.jiapei.junit 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xfwang   
* @date 2016年9月9日 下午4:09:31 
* @version V1.0   
*/
package junit;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName: JUnitTestBase
 * @Description: TODO(这里用一句话描述这个类的作用)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-applicationContext.xml" })
public class JUnitTestBase extends AbstractJUnit4SpringContextTests{// AbstractTransactionalJUnit4SpringContextTests {
    /**
     * <b>Summary: </b> 复写方法 setDataSource
     * 
     * @param dataSource
     * @see org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests#setDataSource(javax.sql.DataSource)
     */
//    @Override
//    @Resource(name = "dataSource")
//    public void setDataSource(DataSource dataSource) {
//        // TODO Auto-generated method stub
//        super.setDataSource(dataSource);
//    }
}
