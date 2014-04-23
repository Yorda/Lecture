package free.tools;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ReadabilityTest {
	Readability readability;
	Readability result;

	public ReadabilityTest() {
	}

	@Before
	public void setUp() throws Exception {
		readability = new Readability();
		result = new Readability();
	}

	@Test
	public void testRemoveStyle() {
		String html = "<html><head><style>body{margin:0 auto}\n\t.paragraph{color:red;\nfont-size:15px;}</style></head><body></body></html>";
		readability.parseDocument(html);
		readability.removeStyle();
		result.parseDocument("<html><head></head><body></body></html>");
		Assert.assertEquals(readability.getDocument().html(), result.getDocument().html());
	}
	
	@Test
	public void testRemoveScript() {
		String html = "<html><head><script>function hello(){ alert(\"Hello world\"); }</script></head><body><script>function other(){ var a = 1; }</script></body></html>";
		readability.parseDocument(html);
		readability.removeScripts();
		result.parseDocument("<html><head></head><body></body></html>");
		Assert.assertEquals(readability.getDocument().html(), result.getDocument().html());
	}
	
	@Test
	public void testReplaceBrs() {
		String toTest = "<p>Hello</p><br><BR>";
		toTest = toTest.replaceAll(readability.getRegularExpressions().get("replaceBrs"),"</p><p>");
		Assert.assertEquals(toTest,"<p>Hello</p></p><p>");
	}

	@Test
	public void testReplaceFonts() {
		String toTest = "<p><FONT familly=...> Hello</p>";
		toTest = toTest.replaceAll(readability.getRegularExpressions().get("replaceFonts"),"<$1span>");
		Assert.assertEquals(toTest,"<p><span> Hello</p>");
	}
	
	@Test
	public void testGetBodyElement() {
		String html = "<html><head></head><body><h1>Hello</h1></body></html>";
		readability.parseDocument(html);
		Assert.assertEquals("<h1>Hello</h1>",readability.getBodyElement().html());
	}
	
	@Test
	public void testCreateDivElement() {
		Assert.assertEquals("<div></div>",readability.createDivElement().outerHtml());
	}
	
	@Test
	public void testGetArticleTitle() {
		String html = "<html><head><title>Article title</title></head><body><p>lorem ....</p></body></html>";
		readability.parseDocument(html);
		Assert.assertEquals("Article title",readability.getArticleTitle().html());
	}
	
	@Test
	public void testEmptyArticle() {
		result.parseDocument("<div id=\"readability-content\"><p>Sorry, extracting article was not possible.</p></div>");
		readability.parseDocument(readability.emptyArticle().outerHtml());
		//Assert.assertEquals(result.getDocument().html(),
		//		readability.getDocument().html());
	}
	
	
}
