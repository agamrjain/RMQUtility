package application;

import java.util.List;

public class CommandClassNew {

	String key;
	String type;
	List<String> prop1DisplayNames;
	boolean isCobDate;
	boolean isMktSnapCd;
	boolean isEventType;
	int order;
	List<String> queue;

	public CommandClassNew(String key, String type, List<String> prop1DisplayNames, boolean isCobDate,
			boolean isMktSnapCd, boolean isEventType, int order) {
		super();
		this.key = key;
		this.type = type;
		if (prop1DisplayNames!=null) {
			this.prop1DisplayNames = prop1DisplayNames;
		}
		this.isCobDate = isCobDate;
		this.isMktSnapCd = isMktSnapCd;
		this.isEventType = isEventType;
		this.order = order;
		
		// 0 order --> prop1, cobdt
		// 1 order --> cobdt, prop1
	}

	public String getKey() {
		return key;
	}

	public String getType() {
		return type;
	}

	public List<String> getProp1DisplayNames() {
		return prop1DisplayNames;
	}

	public boolean isCobDate() {
		return isCobDate;
	}

	public boolean isMktSnapCd() {
		return isMktSnapCd;
	}

	public boolean isEventType() {
		return isEventType;
	}

	public String generateCommandStructure(String type, String prop1, String value1, String cobDate, String mktSValue,
			String eventType) {

		StringBuilder c = new StringBuilder();

		c.append("<command>");
		c.append("<type>"+type+"</type>");

		if (order == 0) {
			if (prop1!=null && value1!=null) {
				c.append(getPropertyValueString(prop1, value1));
			}
			if (isCobDate) {
				c.append(getPropertyValueString(Constants.COB_DATE_IN_COMMAND, cobDate));
			}
			if (isMktSnapCd()) {
				c.append(getPropertyValueString(Constants.MKT_S_CD_IN_COMMAND, mktSValue));
			}
			if (isEventType()) {
				c.append("<event_type>"+ eventType +"</event_type>");
			}

		} else if (order == 1) {
			if (isCobDate) {
				c.append(getPropertyValueString(Constants.COB_DATE_IN_COMMAND, cobDate));
			}
			if (prop1DisplayNames != null) {
				c.append(getPropertyValueString(prop1, value1));
			}

		}
		c.append("</command>");
		return c.toString();

	}

	private String getPropertyValueString(String propName, String value) {
		StringBuilder c = new StringBuilder();

		c.append("<property>" + propName + "</property>");
		c.append("<value>" + value + "</value>");

		return c.toString();

	}
}
