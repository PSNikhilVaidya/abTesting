package com.abtesting.rest.apis.util;

public class GenericResponse {

	private boolean status;
	private int reasonCode;
	private String reason;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(int reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Override
	public String toString() {
		return "GenericResponse [status=" + status + ", reasonCode=" + reasonCode + ", reason=" + reason + "]";
	}
}
