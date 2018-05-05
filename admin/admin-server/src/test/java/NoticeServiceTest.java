import com.fanfandou.admin.api.operation.entity.DeviceType;
import com.fanfandou.admin.api.operation.entity.GameUpdateConfig;
import com.fanfandou.admin.api.operation.entity.GameVersionCheck;
import com.fanfandou.admin.api.operation.entity.Notice;
import com.fanfandou.admin.api.operation.service.NoticeService;
import com.fanfandou.admin.api.operation.service.PatchService;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.exception.ServiceException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wangzhenwei on 2016/7/27.
 * Description test.
 */
public class NoticeServiceTest extends BaseLogger {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private PatchService patchService;


    @Test
    public void test() throws ServiceException {
        /*int gameId = 1;
        int siteId = 2;
        int areaId = 21;
        String siteIds = "%,"+siteId+",%";
        System.out.println(siteIds);*/
        GameVersionCheck check = patchService.checkGameVersion(DeviceType.ANDROID, GameCode.getById(27), 34, 100, 1000, "");

        List<GameUpdateConfig> list = check.getResourceUpdateConfigs();
        for (GameUpdateConfig config : list) {
            logger.info("config = " + config.getUpdateFileName());
        }
//        List<Notice> list = noticeService.findByGameSiteAreaId(gameId,siteId,areaId);
//        System.out.println(list);
    }


}
