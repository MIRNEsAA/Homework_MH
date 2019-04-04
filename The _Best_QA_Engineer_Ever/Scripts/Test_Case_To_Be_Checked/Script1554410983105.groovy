import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.By as By
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI



//   **********  First Page **************************************
// Open Browser
WebUI.openBrowser('')


// Navigate to Page
WebUI.navigateToUrl("https://qa-engineer.herokuapp.com/")

// Maximaze Window
WebUI.maximizeWindow()

WebDriver driver = DriverFactory.getWebDriver()

// Locate element Enter button
WebElement enter_btn = driver.findElement(By.xpath("//button[@id='enter']"))

// Click on Enter button
enter_btn.click()

//  ***********   Second Page *************************************

// Locate hidden input
WebElement input_hidden = driver.findElement(By.xpath("//input[@name='secret']"))
String  secret_code = input_hidden.getAttribute("value")

// Print out to  check staus of code - for debugging
println(" To check if is taken secret code from DOM:"+ secret_code)

// Locate label called Code
WebElement label_code = driver.findElement(By.xpath("//input[@name='code']"))
// Enter secret code
label_code.sendKeys(secret_code)

// Before click on submit button check if checkbox is checked
WebElement input_ckb = driver.findElement(By.xpath("//input[@type = 'checkbox' and @name = 'robot']"))

//If the checkbox is not selected, click it to select it
if (!input_ckb.isSelected()) {
	println("Checkbox was not selected, clicking it")
	input_ckb.click()
}
else {
	println("Checkbox was selected already, not clicking")
}



// Find  button Submit and click on it
WebElement submit_btn = driver.findElement(By.xpath("//button[@type='submit']"))
submit_btn.click();


// ************  Third Page ***********************************

// Expected Quotes on the page
String Expected_Text_1 = "Famous Quotes";
String Expected_Text_2 = "Awesome Quotes";

/*
 * Locate  elements what displyed text. 
 * Get text 
 */
checkCategoriesAndQuotes(driver)
return



WebElement cat1_parent = driver.findElement(By.xpath("//body/ul/li[1]"))
WebElement cat2_parent = driver.findElement(By.xpath("//body/ul/li[2]"))

/*Calculate sum for all quotes*/
int total =0;
total+=getSumForQuotesGroup(cat1_parent)
total+=getSumForQuotesGroup(cat2_parent)
println("Total for all quotes: "+total)

/* Compare with total on the page. If not equal, throw exception*/
int totalOnPage=getTotalScoreOnPage(driver)

if(total != totalOnPage)
{
	KeywordUtil.markFailed('ERROR: The difference between the site and the calculated value. Investigation is needed')
}

/*Test passed*/


/*-----------------------------------------------------------------------------------------------------------------*/
/* Helper functions
 * 
 *-----------------------------------------------------------------------------------------------------------------*/
/*Calculate sum for a group of quotes*/
int getSumForQuotesGroup(WebElement groupRoot) {

	// To locate rows of quotes
	List < WebElement > quotes_rows = groupRoot.findElements(By.xpath(".//ul/li"))

	int subTotal=0;

	for (int i = 0; i < quotes_rows.size(); i++) {
		// It will retrieve text from second cell
		String celltext2 = quotes_rows.get(i).findElement(By.xpath(".//span[2]")).getText().trim()

		// String to Integer
		int result_celltext2 = Integer.parseInt(celltext2)
		println ("Quote number:" + result_celltext2)
		// Sum all numbers displayed in cells
		subTotal = subTotal + result_celltext2
		
	}
	println("Subtotal:"+ subTotal)
	return subTotal
}


/*Retrieve "Total Score" from the page*/
int getTotalScoreOnPage(WebDriver driver) {

	WebElement body = driver.findElement(By.tagName("body"));
	String bodyText = body.getText();

	println(" Body text:"  + bodyText)

	// Check to see if the "Total score:" is found in the bodyText
	String substring = "Total score:";
	int index_no=bodyText.indexOf(substring)
	if (index_no != -1) {
		println("Found the word: " + substring +
				" at index number: " + index_no);

	} else {
		println("Can't find: " + substring);
		KeywordUtil.markFailed('ERROR: Total Score not found in the page')
	}

	/*
	 * No of characters in the displyed text can be changeble
	 * it will be counted current no of characters.
	 * Also ":" can be diplyed in few places, so
	 * it is found whole part"Total scores : " with numbers
	 */

	int number_of_string_ch= bodyText.length()
	println(" Total number of character in string: " + number_of_string_ch)

	String total_score_number = bodyText.substring(index_no, index_no + (number_of_string_ch-index_no));

	println("total number: " + total_score_number)

	String[] total_scores_parts = total_score_number .split(":",2);
	String part1_text = total_scores_parts[0]; //
	String part2_scores = total_scores_parts[1]; //

	println (" Text part: " + part1_text + "  AND  Score part:" + part2_scores)

	int part2_scores_no= Integer.parseInt(part2_scores.trim())

}

/*Check if all quotes and categories are present*/
void checkCategoriesAndQuotes(WebDriver driver) {
	
	/*Expected category names*/
	String exp_cat_fam = "Famous Quotes";
	String exp_cat_awe = "Awesome Quotes";
	
	/*Expected quotes*/
	String[] array_awesome = [
		"I love deadlines. I love the whooshing sound they make as they fly by.",
		"Beware of low-flying butterflies.",
		"Excellent time to become a missing person.",
		"Nothing so needs reforming as other people's habits.",
		"Do something unusual today. Pay a bill."
	]

	String[] array_famous = [
		"You have taken yourself too seriously.",
		"A classic is something that everyone wants to have read and nobody wants to read.",
		"If your life was a horse, you'd have to shoot it.",
		"You have the capacity to learn from mistakes. You'll learn a lot today.",
		"Yes there is a lot of people doing a great job out there."
	]

	/*Gather first category of quotes*/
	WebElement parent1 = driver.findElement(By.xpath("//body/ul/li[1]"))
	ArrayList<WebElement> cat1 = parent1.findElements(By.xpath(".//li"))
	println("Elements in cat 1:" +cat1.size())

	/*Gather second group of quotes*/
	WebElement parent2 = driver.findElement(By.xpath("//body/ul/li[2]"))
	ArrayList<WebElement> cat2 = parent2.findElements(By.xpath(".//li"))
	println("Elements in cat 2:" +cat2.size())

	/*Check what quotes are in the groups*/
	String cat_name_1 = parent1.findElement(By.xpath(".//strong")).getText().trim()
	String cat_name_2 = parent2.findElement(By.xpath(".//strong")).getText().trim()

	/*Check if all expected categories are present*/
	HashSet<String> testSet = new HashSet<String>()
	testSet.add(cat_name_1)
	testSet.add(cat_name_2)
	if (!testSet.contains(exp_cat_fam) || !testSet.contains(exp_cat_awe)) {
		KeywordUtil.markFailed('ERROR: Quote categories do not match expected')
		println(cat_name_1)
		println(cat_name_2)
		return
	}
	else {
		println("All categories are present")
	}

	/*Get lists of quoutes for each category, regardless order*/
	ArrayList<WebElement> toCheckAwe;
	ArrayList<WebElement> toCheckFam;
	
	if (cat_name_1.equals(exp_cat_awe)) {
		toCheckAwe=cat1
		toCheckFam=cat2
	}
	else {
		toCheckAwe=cat2
		toCheckFam=cat1
	}
	
	/*Get strings from web category elements*/
	ArrayList<String> awe_strings = new ArrayList<String>()
	ArrayList<String> fam_strings = new ArrayList<String>()
	
	for (WebElement we_awe:toCheckAwe) {
		String s = we_awe.getText().trim()
		int pos = s.indexOf("(")
		String fs = s.substring(0,pos).trim()
		awe_strings.add(fs)
		println("Awesome: "+fs)
		
	}
	
	for (WebElement we_fam:toCheckFam) {
		String s = we_fam.getText().trim()
		int pos = s.indexOf("(")
		String fs = s.substring(0,pos).trim()
		fam_strings.add(fs)
		println("Famous: "+fs)
	}
	
	/*Check presence of all awesome quotes*/
	for(String q:array_awesome) {
		if (!awe_strings.contains(q)) {
			println("Error: Missing awesome quote: "+q)
			KeywordUtil.markFailed('ERROR: Not all expected quotes are present')
		}
	}

	/*Check presence of all famous quotes*/
	for(String q:array_famous) {
		if (!fam_strings.contains(q)) {
			println("Error: Missing famous quote: "+q)
			KeywordUtil.markFailed('ERROR: Not all expected quotes are present')
		}
	}
	
	
}












