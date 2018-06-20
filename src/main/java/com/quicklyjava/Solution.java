package com.quicklyjava;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Solution {

	private String searchTerm;
	private String craigslistSearchURL = "https://toronto.craigslist.ca/search?sort=rel&query=";

	// Change the below path as per your computer. Make sure directories in the path exist.
	private final String jsonFilePath = "c:\\temp\\results.json";

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);
		Solution soln = new Solution();
		//get the search term from user through console
		System.out.println("Enter the search term: ");
		soln.setSearchTerm(scanner.nextLine());

		Document searchResultsDoc = soln.searchCraigslist();

		Elements searchResultElements = soln.extractSearchData(searchResultsDoc);

		soln.createSearchResultJson(searchResultElements);

		System.out.println("Result json create at: " + soln.jsonFilePath);

	}

	public Document searchCraigslist() throws IOException {

		return Jsoup.connect(this.craigslistSearchURL + this.searchTerm).get();
	}

	public Elements extractSearchData(Document searchDoc) {

		return searchDoc.getElementsByClass("result-row");
	}

	public void createSearchResultJson(Elements searchDataElements)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<SearchResult> searchReultsList = new ArrayList<SearchResult>();
		searchDataElements.forEach(element -> {
			SearchResult result = new SearchResult();
			result.setTitle(element.getElementsByClass("result-title").first().text());
			result.setDate(element.getElementsByClass("result-date").first().text());
			searchReultsList.add(result);
		});

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(this.jsonFilePath), searchReultsList);
	}
}

class SearchResult {

	private String title;
	private String date;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}