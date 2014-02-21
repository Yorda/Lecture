package free.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Lecture {

	public String $version = "1.7.1-without-multi-page";

	public boolean $convertLinksToFootnotes = false;
	public boolean $revertForcedParagraphElements = true;
	
	public String $articleTitle;
	
	public String $articleContent;
	
	public Document $dom;
	
	// optional - URL where HTML was retrieved
	public URL $url = null;
	
	public boolean $debug = false;
	
	// preserves more content (experimental) added 2012-09-19
	public boolean $lightClean = true;
	
	protected Element $body = null;
	
	// Cache the body HTML in case we need to re-use it later
	protected Element $bodyCache = null;
	
	// 1 | 2 | 4; // Start with all flags set.
	protected Integer $flags = 7;
	
	protected boolean $success = false;
	
	/* constants */
	final int FLAG_STRIP_UNLIKELYS = 1;
	final int FLAG_WEIGHT_CLASSES = 2;
	final int FLAG_CLEAN_CONDITIONALLY = 4;
	
	public Map<String,String> $regexps = new HashMap<String, String>();
	
	private Document document = null;
	
	public Lecture (String html, URL url) {
		
		initRegExps();
		
		this.$url = url;
		
		/* Turn all double br's into p's */
		html = html.replaceAll($regexps.get("replaceBrs")  , "</p><p>");
		html = html.replaceAll($regexps.get("replaceFonts"), "<$1span>");
		
		// TODO Escape html entities to UTF8
		
		if (html.trim().compareTo("") == 0) {
			html = "<html></html>";
		}
		
		setDocument(Jsoup.parse(html));
	}
	
	
	public void removeScripts() {
		getDocument().select("script, jscript").remove();
		
	}
	
	/**
	* Runs readability.
	*
	* Workflow:
	* 1. Prep the document by removing script tags, css, etc.
	* 2. Build readability's DOM tree.
	* 3. Grab the article content from the current dom tree.
	* 4. Replace the current DOM tree with the new one.
	* 5. Read peacefully.
	*
	* @return boolean true if we found content, false otherwise
	**/
	public boolean init() {
		//removeScripts();
		

		/*Element element = document.body();
		
		if (element != null) {
			if ($bodyCache == null) {
				$bodyCache = element;
			}

			if ($body == null) {
				$body = element;
			}
		}*/
		
		prepareDocument();
		return false;
	}
	
	/**
	 * Prepare the HTML document for readability to scrape it. This includes
	 * things like stripping javascript, CSS, and handling terrible markup.
	 * 
	 * @return void
	 **/
	protected void prepareDocument() {
	/**
	* In some cases a body element can't be found (if the HTML is totally hosed for example)
	* so we create a new body node and append it to the document.
	*/

		
	getDocument().select("style").remove();
	
	getDocument().select("script, jscript").remove();
	
	//$this->body->setAttribute('id', 'readabilityBody');*/
	
	/* Remove all style tags in head */
	/*$styleTags = $this->dom->getElementsByTagName('style');
	for ($i = $styleTags->length-1; $i >= 0; $i--)
	{
		$styleTags->item($i)->parentNode->removeChild($styleTags->item($i));
	}*/

/* Turn all double br's into p's */
/* Note, this is pretty costly as far as processing goes. Maybe optimize later. */
//document.body.innerHTML = document.body.innerHTML.replace(readability.regexps.replaceBrs, '</p><p>').replace(readability.regexps.replaceFonts, '<$1span>');
// We do this in the constructor for PHP as that's when we have raw HTML - before parsing it into a DOM tree.
// Manipulating innerHTML as it's done in JS is not possible in PHP.
}

	private void initRegExps() {
		$regexps.put("unlikelyCandidates", "/combx|comment|community|disqus|extra|foot|header|menu|remark|rss|shoutbox|sidebar|sponsor|ad-break|agegate|pagination|pager|popup/i");
		$regexps.put("okMaybeItsACandidate", "/and|article|body|column|main|shadow/i");
		$regexps.put("positive", "/article|body|content|entry|hentry|main|page|attachment|pagination|post|text|blog|story/i");
		$regexps.put("negative", "/combx|comment|com-|contact|foot|footer|_nav|footnote|masthead|media|meta|outbrain|promo|related|scroll|shoutbox|sidebar|sponsor|shopping|tags|tool|widget/i");
		$regexps.put("divToPElements", "/<(a|blockquote|dl|div|img|ol|p|pre|table|ul)/i");
		$regexps.put("replaceBrs", "/(<br[^>]*>[ \n\r\t]*){2,}/i");
		$regexps.put("replaceFonts", "/<(\\/?)font[^>]*>/i");
		$regexps.put("normalize", "/\\s{2,}/");
		$regexps.put("killBreaks", "/(<br\\s*\\/?>(\\s|&nbsp;?)*){1,}/");
		$regexps.put("video", "!//(player\\.|www\\.)?(youtube|vimeo|viddler)\\.com!i");
		$regexps.put("skipFootnoteLink", "/^\\s*(\\[?[a-z0-9]{1,2}\\]?|^|edit|citation needed)\\s*$/i");
	}
	
	/**
	 * Get article title element
	 * 
	 * @return DOMElement
	 */
	public Element getTitle() {
		// return $this->articleTitle;
		return null;
	}

	/**
	 * Get article content element
	 * 
	 * @return DOMElement
	 */
	public Element getContent() {
		// return $this->articleContent;
		return null;
	}
	
	public static void main(String[] a) throws Exception {
		File f = new File("D:/tmp/readability_test.htm");
		FileReader reader = new FileReader(f);
		char[] chars = new char[(int) f.length()];
		reader.read(chars);
		String html = new String(chars);
		Lecture lire = new Lecture(html, null);
		lire.init();
		reader.close();
		
		// Write test result
		FileWriter fw = new FileWriter("D:/tmp/readability_modified.htm", true);
		fw.write(lire.getDocument().html());
		fw.close();

		
		/*String s = "<html></html>";
		Document document =Jsoup.parse(s);
		Element $body = document.body();
		
		if ($body == null) {
			$body = document.createElement("body");
			// $this->dom->documentElement->appendChild($this->body);
			System.err.println(document.html());
		}*/
		
	}


	public Document getDocument() {
		return document;
	}


	public void setDocument(Document document) {
		this.document = document;
	}
}
