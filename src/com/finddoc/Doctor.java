package com.finddoc;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("Doctor")
public class Doctor extends IBMDataObject {
	public static final String CLASS_NAME = "Docotor";
	private static String NAME = "HOSPITAL";

	/**
	 * Gets the name of the Doctor.
	 * 
	 * @return String itemName
	 */
	public String getName() {
		/* Check the Call type, if its H then Hospital, else Doctor */
		if (FullscreenActivity.callType.equals("H"))
			NAME = "HOSPITAL";
		else if(FullscreenActivity.callType.equals("S"))
			NAME = "SPECIALITY";
		else
			NAME = "FULLNAME";

		return (String) getObject(NAME);
	}

	/**
	 * Sets the name of a list item, as well as calls setCreationTime().
	 * 
	 * @param String
	 *            itemName
	 */
	public void setName(String itemName) {
		/* Check the Call type, if its H then Hospital, else Doctor */
		if (FullscreenActivity.callType.equals("H"))
			NAME = "HOSPITAL";
		else if(FullscreenActivity.callType.equals("S"))
			NAME = "SPECIALITY";
		else
			NAME = "FULLNAME";
		setObject(NAME, (itemName != null) ? itemName : "");
	}

	/**
	 * When calling toString() for an item, we'd really only want the name.
	 * 
	 * @return String theItemName
	 */
	public String toString() {
		String theItemName = "";
		theItemName = getName();
		return theItemName;
	}
}
