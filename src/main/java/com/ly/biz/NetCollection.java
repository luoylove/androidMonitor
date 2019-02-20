package com.ly.biz;

import static com.ly.constant.Constant.*;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.ly.utils.BizException;
import com.ly.utils.DosExec;
import com.ly.utils.LogUtil;
import com.ly.utils.Util;

public class NetCollection {

	static Logger log = LogUtil.getLogger(NetCollection.class);

	private Double nowNetDataReceive0 = 0D; // 接收数据

	private Double nowNetDataReceive1 = 0D; // 接收数据

	private Double nowNetDataTransmit0 = 0D; // 发送数据

	private Double nowNetDataTransmit1 = 0D; // 发送数据

	private Double nowNetDataReceiveSum = 0D;

	private Double nowNetDataTransmitSum = 0D;

	private Double runNum = 1D;

	private String packageName;

	private String ADB_START_CMD;

	public NetCollection(String deviceId, String packageName) {
		super();
		this.packageName = packageName;

		ADB_START_CMD = ADB_CMD_START + deviceId + " shell ";
	}

	/**
	 * 接收数据，传输数据, 接收数据平均值，传输数据平均值， 总监控次数
	 * 
	 * @return
	 */
	public Double[] getNetData() {
		Double netDatas[] = new Double[5];

		String cmd = ADB_START_CMD + ADB_NET_CMD + getAppUid(packageName) + ADB_NET_CMD_END;

		String result = DosExec.executeCmd(cmd);

		log.info("[net]查询命令：" + cmd);
		log.info("[net]查询结果：" + result);

		String resultsTmp[] = result.trim().split(" 0x0 ");

		String results[] = new String[2];

		results[0] = resultsTmp[1];
		results[1] = resultsTmp[2];

		Double rx0 = Double.valueOf(results[0].split(" ")[2]);
		Double rx1 = Double.valueOf(results[1].split(" ")[2]);

		Double tx0 = Double.valueOf(results[0].split(" ")[4]);
		Double tx1 = Double.valueOf(results[1].split(" ")[4]);

		if (nowNetDataReceive0 == 0 && nowNetDataReceive1 == 0 && nowNetDataTransmit0 == 0
				&& nowNetDataTransmit1 == 0) {
			nowNetDataReceive0 = rx0;
			nowNetDataReceive1 = rx1;
			nowNetDataTransmit0 = tx0;
			nowNetDataTransmit1 = tx1;
		} else {
			runNum++;

			nowNetDataReceiveSum = (rx0 - nowNetDataReceive0) + (rx1 - nowNetDataReceive1);
			nowNetDataTransmitSum = (tx0 - nowNetDataTransmit0) + (tx1 - nowNetDataTransmit1);
		}

		Double receiveData = Util.dFormat(nowNetDataReceiveSum / 1024, 2);

		Double transmitData = Util.dFormat(nowNetDataTransmitSum / 1024, 2);

		netDatas[0] = receiveData;

		netDatas[1] = transmitData;

		netDatas[2] = Util.dFormat(receiveData / runNum, 2);

		netDatas[3] = Util.dFormat(transmitData / runNum, 2);

		netDatas[4] = runNum;

		log.info("[net]采集数据结果：" + Arrays.toString(netDatas));

		return netDatas;
	}

	private static String getAppUid(String packageName) {
		String cmd = GET_APP_UID_CMD + packageName + GET_APP_UID_CMD_END;
		try {
			String result = MeterCollection.isConnectAdbAndStartApp(DosExec.executeCmd(cmd));
			String results[] = result.trim().split(" ");

			if (results[0].contains("userId")) {
				return results[0].substring(7);
			} else {
				throw new RuntimeException("获取appuid失败：" + result);
			}
		} catch (BizException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
}
