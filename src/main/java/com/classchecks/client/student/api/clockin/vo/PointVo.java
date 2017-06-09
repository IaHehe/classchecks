/**
 * Copyright © 2017. by Tengyu Ma
 * CreateTime: 2017年6月1日 上午4:33:06 
 * @author  Tengyu Ma  mty2015@126.com 
 * @version V1.0.0
 */
package com.classchecks.client.student.api.clockin.vo;

/**
 * 经纬度坐标
 * 
 * @author matengyu
 */
public class PointVo {
	private double lng;
	private double lat;

	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PointVo [lng=" + lng + ", lat=" + lat + "]";
	}

}
