package me.aurous.searchers;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Example program to list links from a URL.
 */
public class YouTubeSearch {
	public static void main(final String[] args) throws IOException {

		final String url = "https://www.youtube.com/results?search_query=lana+del+ray";
		print("Fetching %s...", url);

		final Document doc = Jsoup.connect(url).get();
		final Elements links = doc.select("a[href]");

		print("\nLinks: (%d)", links.size());
		for (final Element link : links) {
			if (link.attr("abs:href").contains("&list=")
					|| (link.attr("abs:href").contains("watch?v=")
							&& (link.text().length() > 0)
							&& !link.text().contains("Play all")
							&& !link.text().contains("views")
							&& !link.text().contains("vevo") && !link.text()
							.contains("Play now"))) {
				print(" * a: <%s>  (%s)", link.attr("abs:href"), link.text());
			}
		}
	}

	private static void print(final String msg, final Object... args) {
		System.out.println(String.format(msg, args));
	}
}
