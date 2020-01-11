package com.cscie97.store.authentication;

/**
 * Face or voice print associated with users within the
 * authentication service
 */
class BioPrint {
	private String id;
	private String value;

	BioPrint(String id, String value){
		this.id = id;
		this.value = value;
	}

	/**
	 * Method for determining if this print matches the passed print value
	 * @param value print to check
	 * @return true if prints match, false otherwise
	 */
	boolean printMatches(String value) {
		return (this.value.equals(value));
	}

	@Override
	public String toString(){
		return "Bio print: " +
				"\n\tid: " + id +
				"\n\tvalue: " + value;
	}
}
