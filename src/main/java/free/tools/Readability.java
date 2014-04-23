package free.tools;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Readability {

	private Document document = null;
	private Map<String,String> regularExpressions = new HashMap<String, String>();
	
	public Readability() {
		initRegularExpressions();
	}

	public void createReadableContent() {
		// Element overlay        = createDivElement();
		// Element innerDiv       = createDivElement();
		// Element articleTitle   = getArticleTitle();
		Element articleContent = getArticleContent() ;
		if (articleContent == null
				|| articleContent.html().trim().length() == 0) {
			articleContent = emptyArticle() ;
		}
	}

	public Element emptyArticle() {
		Element element = createDivElement();
		element.attr("id", "readability-content");
		element.html("<p>Sorry, extracting article was not possible.</p>");
		return element;
	}
	
	public Element getArticleTitle() {
		Element h1 = Jsoup.parse("<h1></h1>").select("h1").first();
		h1.html(getDocument().select("title").first().html());
		return h1;
	}
	
	public Element getArticleContent() {
		
		HashMap<Element, Integer> scores = new HashMap<Element, Integer>();
		
		for (Element el : getDocument().select("body").select("*")) {
			
			if (el.tag().getName().equals("p")
					|| el.tag().getName().equals("td")
					|| el.tag().getName().equals("div")) {
				scores.put(el, 0);
			}
		}
		
		return null;
	}
	
	public void parseDocument(String html) {
		html = html.replaceAll(regularExpressions.get("replaceBrs")  , "</p><p>");
		html = html.replaceAll(regularExpressions.get("replaceFonts"), "<$1span>");
		
		setDocument(Jsoup.parse(html));
	}

	public void prepareDocument() {
		removeStyle();
		removeScripts();
	}

	public void removeStyle() {
		getDocument().select("style").remove();
	}

	public void removeScripts() {
		getDocument().select("script, jscript").remove();
	}
	
	public Element getBodyElement() {
		Element body = getDocument().body();
		return body;
	}

	public Element createDivElement() {
		Element element = Jsoup.parse("<div></div>").select("div").first();
		return element;
	}
	
	private void initRegularExpressions() {
		getRegularExpressions().put("unlikelyCandidates", "/combx|comment|community|disqus|extra|foot|header|menu|remark|rss|shoutbox|sidebar|sponsor|ad-break|agegate|pagination|pager|popup/i");
		getRegularExpressions().put("okMaybeItsACandidate", "/and|article|body|column|main|shadow/i");
		getRegularExpressions().put("positive", "/article|body|content|entry|hentry|main|page|attachment|pagination|post|text|blog|story/i");
		getRegularExpressions().put("negative", "/combx|comment|com-|contact|foot|footer|_nav|footnote|masthead|media|meta|outbrain|promo|related|scroll|shoutbox|sidebar|sponsor|shopping|tags|tool|widget/i");
		getRegularExpressions().put("divToPElements", "/<(a|blockquote|dl|div|img|ol|p|pre|table|ul)/i");
		getRegularExpressions().put("replaceBrs", "(?iu)(<br[^>]*>[ \n\r\t]*){2,}");
		getRegularExpressions().put("replaceFonts", "(?iu)<(\\/?)font[^>]*>");
		getRegularExpressions().put("normalize", "/\\s{2,}/");
		getRegularExpressions().put("killBreaks", "/(<br\\s*\\/?>(\\s|&nbsp;?)*){1,}/");
		getRegularExpressions().put("video", "!//(player\\.|www\\.)?(youtube|vimeo|viddler)\\.com!i");
		getRegularExpressions().put("skipFootnoteLink", "/^\\s*(\\[?[a-z0-9]{1,2}\\]?|^|edit|citation needed)\\s*$/i");
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Map<String,String> getRegularExpressions() {
		return regularExpressions;
	}

}
