package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Equipment {

	private String os = null;

	private String loginid = null;

	@XmlElement(name = "newly-establish")
	private String newlyEstablish;

	public Equipment() {
		super();
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void addNewlyEstablish() {
		this.newlyEstablish = new String("");
	}

	public void delNewlyEstablish() {
		this.newlyEstablish = null;
	}

	@Override
	public String toString() {
		return "Equipment [platform=" + platform + ", os=" + os + ", firmware=" + firmware + ", loginid=" + loginid
				+ ", password=" + password + ", newlyEstablish=" + newlyEstablish + "]";
	}

}
