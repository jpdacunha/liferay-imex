package com.liferay.imex.shell.client.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TableBuilder {
	
	List<String[]> rows = new LinkedList<String[]>();
	
	public void addHeaders(String... headers) {
		
		rows.add(format(headers));
	}

	public void addRow(String... cols) {
		rows.add(format(cols));
	}
	
	private String[] format(String... values) {
		
		String[] copy = new String[values.length];
		int i = 0;
	    for( String s : values ) {
	        copy[i] = "| " + s;
	    	i++;
	    }
		return copy;
	}

	private int[] colWidths() {
		int cols = -1;

		for (String[] row : rows)
			cols = Math.max(cols, row.length);

		int[] widths = new int[cols];

		for (String[] row : rows) {
			for (int colNum = 0; colNum < row.length; colNum++) {
				widths[colNum] = Math.max(widths[colNum], StringUtils.length(row[colNum]));
			}
		}

		return widths;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();

		int[] colWidths = colWidths();

		for (String[] row : rows) {
			for (int colNum = 0; colNum < row.length; colNum++) {
				buf.append(StringUtils.rightPad(StringUtils.defaultString(row[colNum]), colWidths[colNum]));
				buf.append(' ');
			}

			buf.append('\n');
		}

		return buf.toString();
	}

	public void print() {
		System.out.println(this.toString());	
	}

}
