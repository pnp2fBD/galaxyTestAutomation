package com.ssp.uxp_pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.ssp.support.DateTimeUtility;
import com.ssp.support.Log;
import com.ssp.utils.ElementLayer;
import com.ssp.utils.GenericUtils;
import com.ssp.utils.WaitUtils;

/**
 * Customer Dashboard Page consists all the policy/premium related documents *
 */
public class NewQuotePage extends LoadableComponent<NewQuotePage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  private String radio_button_value = null;
  public ElementLayer uielement;

  public final String ERROR_MSG_PAYOR_ALLFIELDS =
      "Please enter a Lastname, Firstname, and either a Date of Birth or Postcode";
  public final String ERROR_MSG_PAYOR_POSTCODE_OR_DOB =
      "Please enter either a Date of Birth or Postcode";
  public final String ERROR_MSG_PAYOR_LASTNAME = "Please enter a Lastname";
  public final String ERROR_MSG_ADD_ACCOUNT_DETAILS =
      "Please enter valid sort code and account number";
  public final String INTERESTED_PARTIES_REMOVE_DESC1 = "You are about to remove";
  public final String INTERESTED_PARTIES_REMOVE_DESC2 = "Do you wish to continue?";
  public final String ERROR_MSG_ACCOUNT_DETAILS = "A bank account must be entered.";
  public final String ERROR_MSG_NO_ACCOUNT_DETAILS = "No Account Details Exists";

  public String radCover = "div[id*='QUE_15D8DD38C9D9D4FD208240']>div>fieldset>div";
  public String radClaims = "label[for*='QUE_F7F54EFB71FFA51525068']>span"; // "div[id*='QUE_F7F54EFB71FFA51525068']>div>b>b>div>div";
  public String radPersonal = "label[for*='QUE_F7F54EFB71FFA51525071']>span";// "div[id*='QUE_F7F54EFB71FFA51525071']>div>b>b>div>div";
  public String radBicycle = "label[for*='QUE_F7F54EFB71FFA51525074']>span";// "div[id*='QUE_F7F54EFB71FFA51525074']>div>b>b>div>div";
  public String radHighrisk = "label[for*='QUE_F7F54EFB71FFA51525077']>span"; // "div[id*='QUE_F7F54EFB71FFA51525077']>div>b>b>div>div";
  public String radInsAddress = "fieldset[id*='3661A386F2BF3E14986893']>div label"; // "fieldset[id*='3661A386F2BF3E14986893']>div.radio";
  public String radUKResident = "div[id*='QUE_3661A386F2BF3E141022445']>div>div>div>div>label"; // "div[id*='QUE_3661A386F2BF3E141022445']>div>div>div";
  public String radBankrupcy = "div[id*='p4_QUE_3661A386F2BF3E141022451']>div>div>div>label"; // "div[id*='p4_QUE_3661A386F2BF3E141022451']>div>div>div";
  public String radAgree =
      "#C2__p4_QUE_921BCE84FE9FC3B2469614 div:nth-child(1) > label[for='C2__QUE_921BCE84FE9FC3B2469614_0']";
  public String impDocuments =
      "div[id*='FMT_3661A386F2BF3E141531426']>div>div[id*='BUT_712BAE31A6D7FEBA1490274']>div>div>div>label";
  public String spinner = "div.spinning-on-load-bg-table-active";
  public String tbl_insurance_covers_CNT = "div[id*='FMT_728A190A046539421182844'] tbody>tr";
  public String tbl_insurance_covers_Bld = "div[id*='FMT_728A190A04653942826973'] tbody>tr";
  public String tbl_insurnace_covers_AddOns = "div[id*='FMT_728A190A046539421184112'] tbody>tr";
  public String ins_BldCover_excessAmt = "select[name*='BUIDINGSCOVER'][name*='EXCESS']";
  public String popup_edit_ins_Bld = "div[id*='FMT_728A190A046539421316998'].modal-content";
  public String rad_ins_HRcover = "div[id*='p4_QUE_12B9E4694CE1FE92954109'] label span";
  public String PopupWnd_ins_HRCover = "div[id*='FMT_728A190A046539421661764'].modal-dialog";
  public String PopupWnd_ins_GCcover = "div[id*='FMT_728A190A046539421871965'].modal-dialog";
  public String PopupWnd_ins_TEcover = "div[id*='FMT_728A190A046539422029713'].modal-dialog";
  public String rad_ins_PC = "div[id*='p4_QUE_F7F54EFB71FFA51525074'] label span";
  public String PopupWnd_ins_PC = "div[id*='FMT_728A190A046539422229728'].modal-dialog";
  public String rad_ins_PIcover = "div[id*='p4_QUE_F7F54EFB71FFA51525071']>div>div>div";
  public String PoupWnd_ins_PI = "div[id*='FMT_728A190A046539422175313'].modal-dialog";
  public String Page_Spin = "div.spinning-on-load-bg-table-active";
  public String txtmsg_unknwn_error =
      "div[id*='FMT_9E775789D61887FF4424404']>div>div>div>div>div>div.col-md-11";
  public String txt_Nosearchresultdisp = "#C2__TXT_B081B9E5856613271755640 p";
  public String table_intPartyTR = "tr[id*='C2__p0_TBL_29E0D21D09DB6812743173_R']";

  public String addInterestPartymodal = "#C2__HEAD_65369060E37BB4AE5003773";
  public String addInterestContactType = "#C2__p1_QUE_E36EFB44BDE033C56651708>div>label";
  public String addInterestContatcTypeDrpdwn = "#C2__QUE_E36EFB44BDE033C56651708";
  public String addInterestSearchBtn = "button.btn.btn-default#C2__BUT_29E0D21D09DB6812743210";
  public String addInterestPartyModalPane = "#C2__row_HEAD_65369060E37BB4AE4848856";
  public String addInterestSearchResultPane =
      "div#C2__FMT_E36EFB44BDE033C56426563.modal-body.clearfix.errormsg_position";
  public String addInterestsearchforContact = "#C2__FMT_E36EFB44BDE033C56660021";
  public String addInterestCancelBtn = "#C2__BUT_29E0D21D09DB6812621225";
  public String addInterestCreateAddInterest = "#C2__BUT_88B187603655E68A1064276";

  public String removeDescription = "#C2__TXT_E36EFB44BDE033C56198500>div>div>p";

  public String radGender = "fieldset[id*='FS_QUE_B5F37DDFDCE76BB21217328'] .radio label";
  public String radPreferences = "fieldset[id*='QUE_B5F37DDFDCE76BB21217334'] .radio label";

  public String but_personSave = "button[id*='SaveBtn_Create_New_Personal_Contact']";

  public String pane_IP_searchResults = "div[id*='E36EFB44BDE033C56426563']";
  public String but_IP_searchResults_Cnl = "button[title='Cancel']";

  public String JointPolicyHolderSearch = "button[title='Search']";
  public String firstNameOfPolicyHolderTable = "#C2__QUE_C6ED9305165D91DC2171257_R1";

  public String firstCheckOfPolicyHolderTable =
      "input[type='checkbox'][id*='C2__QUE_65369060E37BB4AE3541860_0_R1']";

  public String cssBinIcon = "button[class='trash']";
  public String cssBtnYesToRemovePolicyHolder = "button[id='C2__RemovePolicyholder_Yes']";
  public String cssBtnYesToswitchMainPolicyHolder = "button[id='C2__ChangeMainPolicyholder_Yes']";
  public String cssViewButton = "button[class*='ViewPolicyHolder']";
  public String cssEditButton = "button[title='Edit']";

  public String cssComponentDropDown = "select[name*='COMPONENT']";

  public String cssDeSelectButton = "button[title='De-select']";
  public String cssCancelButton = "button[title='Cancel']";

  public String cssConfirmNewPayorButton = "button[id*='BUT_1F419D606979599B1231397']";

  public String cssPaySchedColHeader = "div[id='C2__TXT_039B9D213617E7CA806962'] .table_head";
  public String cssPaySchedDueDates = "span[id*='9C281AC77BF4F72E2122776']";
  public String cssPaySchedCalculatedPremium = "span[id*='9C281AC77BF4F72E2122778']";
  public String cssPaySchedTaxes = "span[id*='9C281AC77BF4F72E2122780']";
  public String cssPaySchedCharges = "span[id*='9C281AC77BF4F72E2122782']";
  public String cssPaySchedChargesTax = "span[id*='9C281AC77BF4F72E2134770']";
  public String cssPaySchedInstallments = "span[id*='9C281AC77BF4F72E2134773']";
  public String cssPaySchedAmountPayable = "span[id*='9C281AC77BF4F72E2134776']";

  public String xpathAccSelectBtnOfAccDetails =
      "//table[@summary='BankDetails']//tr[.//span[text()='${SORT_CODE}']][.//span[text()='${BRANCH}']][.//span[text()='${ACC_NAME}']][.//span[text()='${ACC_NO}']]//button";

  public String cssTandCConfirmationdialog = "#C2__C1__FMT_9F00A31B5618E9DE526536";

  @FindBy(css = "fieldset[id*='3661A386F2BF3E14986893']>div.radio")
  WebElement radinsurance_ele;

  @FindBy(css = "div[id*='QUE_3661A386F2BF3E141022445']>div>div>div")
  WebElement radUKResident_ele;

  @FindBy(css = "[title='Back to Dashboard']")
  WebElement lnkBacktoDashboard;

  @FindBy(css = "#C2__BUT_F7F54EFB71FFA51527230")
  WebElement lnkBacktoDashboardRnl;

  @FindBy(css = "#C2__QUE_F7F54EFB71FFA51524509")
  WebElement selectnoofbedroom;

  @FindBy(css = "#C2__QUE_F7F54EFB71FFA51524629")
  WebElement selectpropertytype;//

  @FindBy(css = ".run-inner-rule-policy-view.table-row-active.polices-row :nth-child(3)")
  WebElement txtCreatedPolicy;

  @FindBy(css = "#C2__BUT_FE031EDBB6A4EA08804112")
  WebElement btnAcceptMTA;

  @FindBy(css = "#C2__ID_DIRECT_DEBIT_CONFIRM")
  WebElement btnConfirmMTA;

  @FindBy(css = "#C2__B2_QUE_FD051A198CB3861E1713415[title='ADD ALL']")
  WebElement btnAddAllMTA;

  @FindBy(css = "#C2__BUT_FD051A198CB3861E1713417[title='Next']")
  WebElement btnAddAllMTANext;

  @FindBy(css = "#C2__BUT_728A190A046539422616673[title='Buy']")
  WebElement btnBuyMTA;

  @FindBy(css = "input[name*='YEAROFBUILD']")
  WebElement txtYearofBuild;

  @FindBy(css = "#C2__QUE_F7F54EFB71FFA51524993")
  WebElement dateOfBirthNewQuote;

  @FindBy(css = "button[id*='BUT_15D8DD38C9D9D4FD403269']") // C2__BUT_15D8DD38C9D9D4FD403269
  WebElement btnNextOne;

  @FindBy(css = "button[id*='BUT_3661A386F2BF3E14799940']")
  WebElement btnPaymentNextone;

  @FindBy(css = "select[name*='CONTENTSINSURANCEHELD']")
  WebElement selectcontinsheld;

  @FindBy(css = "select[name*='BUILDINGSINSURANCEHELD']")
  WebElement selectbuildinsheld;

  @FindBy(css = "button[id*='BUT_21E887F47BE45ECE11009']")
  WebElement btnNextTwo;

  @FindBy(css = "button#C2__BUT_921BCE84FE9FC3B2409611")
  WebElement btnView;//

  @FindBy(css = "button[id*='save-declaration']")
  WebElement btnSaveDeclaration;

  @FindBy(css = "div[id*='FMT_3A056143913CF4954126757_R2']")
  WebElement firstTermAndConditionInList;

  @FindBy(css = "#C2__C1__QUE_3A056143913CF4954126787_R2")
  WebElement firstTermAndConditionTextArea;

  @FindBy(css = "div[id*='FMT_3A056143913CF4954126757_R3']")
  WebElement secondTermAndConditionInList;

  @FindBy(css = "button[title='Edit']")
  WebElement btnEditTermAndCondition;

  @FindBy(css = "span[id*='QUE_3A056143913CF4954126787_R2'][class='editTAC']")
  WebElement selectTAC;

  @FindBy(css = "span[id*='QUE_3A056143913CF4954126787_R3'][class='editTAC']")
  WebElement selectSecondTAC;

  @FindBy(css = "#C2__BUT_F9236B9689BADD9D689570")
  WebElement btnGetQuote;

  @FindBy(css = "#C2__BUT_2BF7614AC3E22D7C1441352[title='Buy']")
  WebElement btnBuy;

  @FindBy(css = "#C2__BUT_728A190A046539422616673[title='Buy']")
  WebElement btnRenewalQuoteBuy;

  @FindBy(css = "button[id*='BUT_3661A386F2BF3E14799940']")
  WebElement btnCustomerPreferenceNext;

  @FindBy(css = "button[id*='BUT_B9E1D767142BE41B422175']")
  WebElement btnPaymentNexttwo;

  @FindBy(css = "button[id*=BUT_8A17B7AD077319CD4115330]")
  WebElement btnAttachTermAndCondition;

  @FindBy(css = "input[type='checkbox'][name*='TERMSANDCONDITIONS']")
  WebElement termsconditions;

  @FindBy(css = "a[id='termsandconditions']")
  WebElement termsAndConditionsSection;

  @FindBy(css = "select[name*='PAYMENTPLAN']")
  WebElement selectpaymentplan;

  @FindBy(css = "select[name*='PAYMENTMETHOD']")
  WebElement selectpaymentmethod;

  @FindBy(css = "button[title='Add Account Details']")
  WebElement but_AddAccDetail;

  @FindBy(css = "input[id='C2__QUE_EE2C7D9B8CC571FB1355397']")
  WebElement txt_AccNumber;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].BUYQUOTE[1].DIRECTDEBITS[1].ACCOUNTDETAILS[1].SORTCODE']")
  WebElement txt_SortCode;

  @FindBy(css = "button[title='Check Account']")
  WebElement but_CheckAccount;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].BRANCH_READONLY']")
  WebElement fldBranch;

  @FindBy(css = "button[id='C2__ID_DIRECT_DEBIT_SAVE']")
  WebElement but_AddAccSave;

  @FindBy(css = "#C2__p0_TBL_EE2C7D9B8CC571FB2770383_R1 td:nth-child(2)")
  WebElement tableAccDetailsBranch;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].BUYQUOTE[1].DIRECTDEBITS[1].ACCOUNTDETAILS[1].ACCOUNTNAME']")
  WebElement txt_AccName;

  @FindBy(css = "button[id*='CONFIRM']")
  WebElement btnConfirmPayment;

  @FindBy(css = "div#C2__p1_HEAD_E36EFB44BDE033C54659681.inner-title")
  WebElement txt_interestedParties;

  @FindBy(css = "div[id*='FMT_3661A386F2BF3E141218691'].panel-body.premium-finance")
  WebElement PremiumFinance;

  @FindBy(css = "input[type='checkbox'][name*='PROCESSPAYMENTS']")
  WebElement selectcheckbox;

  @FindBy(css = "button[title='Continue']")
  WebElement eligibilityCheck;

  @FindBy(css = "input[name*='BANKACCOUNTHOLDERNAME']")
  WebElement txtActHolder;

  @FindBy(css = "input[name*='SORTCODE']")
  WebElement txtSortCode;

  @FindBy(css = "input[name*='ACCOUNTNUMBER']")
  WebElement txtActNumber;

  @FindBy(css = "button[title='Validate Bank Details']")
  WebElement btnValidate;

  @FindBy(css = "#C2__C6__QUE_F8495CB9C6ABE93A260890")
  WebElement cmbTypeReverTransac;

  @FindBy(css = "button[id*='Bank_continue']")
  WebElement validationCheck;

  @FindBy(css = "input[type='checkbox'][name*='CONFIRMCONTENT']")
  WebElement confirmcontent;

  @FindBy(css = "button[title='Back to Bank Validation']")
  WebElement backbankvalidation;

  @FindBy(css = "button[id*='BUT_6F266DB023A12C1F523339']")
  WebElement button3StarProduct;

  @FindBy(css = "button.btn.btn-default.pull-right.btn_home_emergency.page-spinner")
  WebElement btnAddHomeEmergency;

  @FindBy(css = "button[id='C2__BUT-RE-CALCULATE']")
  WebElement btnReCalculate;

  @FindBy(css = "button.btn.btn-default.page-spinner[title='Upgrade now']")
  WebElement btnUpgradeNow;

  @FindBy(
      css = "#C2__p4_QUE_921BCE84FE9FC3B2469614 div:nth-child(1) > label[for='C2__QUE_921BCE84FE9FC3B2469614_0']")
  WebElement radAgree_Ele;

  @FindBy(css = "select[name='BUILDINGSINSURANCEHELD']")
  WebElement select_BLDinsheld;

  @FindBy(css = "div[id*='FMT_728A190A046539421316998'].modal-content")
  WebElement ins_BLDCover_EditPopUp;

  @FindBy(css = "button[id*='BUT_8735158B98A3B50E198519']")
  WebElement ins_BldCover_EditSaveBut;

  @FindBy(css = "div[id*='FMT_728A190A046539421661764'].modal-dialog")
  WebElement ins_HRcoverPopUp;

  @FindBy(css = "select[name*='RISKITEMSCOVER']")
  WebElement ins_HRcoverRisk1;

  @FindBy(css = "select[name*='HIGHRISKITEM'][name*='CATEGORY']")
  WebElement ins_HR_Category;

  @FindBy(css = "input[name*='HIGHRISKITEM'][name*='DESCRIPTION']")
  WebElement ins_HR_Desc;

  @FindBy(css = "input[name*='HIGHRISKITEM'][name*='VALUE']")
  WebElement ins_HR_itemVal;

  @FindBy(css = "button[id*='BUT_9AF9ECE9A7A9E74D646847']")
  WebElement ins_HR_SaveBut;

  @FindBy(css = "div[id*='FMT_728A190A046539421871965'].modal-dialog")
  WebElement ins_GCcover_PopUp;

  @FindBy(css = "select[name*='GARDENCOVER'][name*='COVERLIMIT']")
  WebElement ins_GCcover_Limit;

  @FindBy(css = "button[id*='BUT_728A190A046539421871282']")
  WebElement ins_GCcover_saveBut;

  @FindBy(css = "div[id*='FMT_728A190A046539422029713'].modal-dialog")
  WebElement ins_TEcover_Popup;

  @FindBy(css = "select[name*='TECHNOLOGYANDENTERTAINMENT'][name*='COVERLIMIT']")
  WebElement ins_TEcover_Limit;

  @FindBy(css = "button[id*='BUT_728A190A046539422029662']")
  WebElement ins_TEcover_saveBut;

  @FindBy(css = "div[id*='FMT_728A190A046539422229728'].modal-dialog")
  WebElement ins_PCcover_Popup;

  @FindBy(css = "div[id*='p4_QUE_F7F54EFB71FFA51525074']>div>div>div")
  WebElement ins_PC_cover_RadioBut;

  @FindBy(css = "select[name*='PEDALCONTENTS'][name*='NUMBEROFCYCLES']")
  WebElement ins_PCcover_UnSp_cycleCount;

  @FindBy(css = "input[name*='PEDALCONTENTS'][name*='PEDALCYCLECOVERREQUIRED']")
  WebElement ins_PCcover_coverAmt;

  @FindBy(css = "select[name*='PEDALCONTENTS'][name*='PEDALCYCLENUMBER']")
  WebElement ins_PCcover_Specified_cyclecount;

  @FindBy(css = "input[id*='QUE_B9E1D767142BE41B1646795_R1']")
  WebElement ins_PCcover_Description;

  @FindBy(css = "input[id*='QUE_B9E1D767142BE41B1646797_R1']")
  WebElement ins_PCcover_value;

  @FindBy(css = "button[id*='BUT_9AF9ECE9A7A9E74D632553']")
  WebElement ins_PCcover_SaveBut;

  @FindBy(css = "#C2__BUT_722823B5DB79AD341407188")
  WebElement btn_SaveQuoteAfterTerms;

  @FindBy(css = "div[id*='FMT_728A190A046539422175314'].modal-content")
  WebElement ins_PIcover_Popup;

  @FindBy(css = "select[id*='QUE_728A190A046539422176671']")
  WebElement ins_PI_coverReq;

  @FindBy(css = "select[id*='QUE_9AF9ECE9A7A9E74D617387']")
  WebElement ins_PI_NoOfItem;

  @FindBy(css = "select[name*='HIGHVALUEPERSONALITEM'][name*='CATEGORY']")
  WebElement ins_PI_Category;

  @FindBy(css = "input[name*='HIGHVALUEPERSONALITEM'][name*='DESCRIPTION']")
  WebElement ins_PI_Desc;

  @FindBy(css = "input[name*='HIGHVALUEPERSONALITEM'][name*='VALUE']")
  WebElement ins_PI_ItemValue;

  @FindBy(css = "button[id*='BUT_9AF9ECE9A7A9E74D604540']")
  WebElement ins_PI_SaveBtn;

  @FindBy(css = "button[id*='BUT_3516874A7C1460DF1409566']")
  WebElement btnDashboard;

  @FindBy(css = "div[id*='p4_QUE_492C3ACACC55FF9C625499']>div>div>div")
  WebElement custDeclaration;

  @FindBy(css = "button#C2__ADD_INTERESTED_PARTY")
  WebElement addInterestedPartyBtn;

  @FindBy(css = "#C2__FMT_E36EFB44BDE033C56426559.modal-dialog")
  WebElement modal_IntParty;

  @FindBy(css = "select[name*='INTERESTEDPARTYOVERLAY[1].CONTACTTYPE']")
  WebElement dropdown_IntParty;

  @FindBy(css = "input[name*='SEARCHFORCONTACT']")
  WebElement txt_serch_fName;

  @FindBy(css = "input[name*='SEARCHCONTACTSECONDNAME']")
  WebElement txt_serch_lName;

  @FindBy(css = "button#C2__BUT_29E0D21D09DB6812743210")
  WebElement but_Search;

  @FindBy(css = "button#C2__BUT_29E0D21D09DB68121111046")
  WebElement but_Search_CorpIP;

  @FindBy(css = "#C2__FMT_CD844DE86150F4B31228584 h1")
  WebElement pane_IntPart;

  @FindBy(css = "div.alert.alert-warning.clearfix[style='display:block !important;'] p.errorClass")
  WebElement txtmsg_warnIP;

  @FindBy(css = "select[name*='INTERESTEDPARTYTYPE']")
  WebElement selIntPartyType;

  @FindBy(css = "input#C2__QUE_29E0D21D09DB6812718811")
  WebElement datePicker_from;

  @FindBy(css = "input#C2__QUE_29E0D21D09DB6812718814")
  WebElement datePicker_To;

  @FindBy(css = "button#C2__BUT_29E0D21D09DB68121013768[value='Save']")
  WebElement but_IntPartysave;

  @FindBy(css = "input#C2__QUE_2AED8243C0FF5F64796873")
  WebElement txt_IntPartCorp_lName;

  @FindBy(css = "input[name*='INTERESTEDPARTYOVERLAY[1].POSTCODE']")
  WebElement txt_IntPartCopr_Pcode;

  @FindBy(css = "#C2__row_ansRowQUE_CD844DE86150F4B31208405 span[class*='GlobalFont Color1']")
  WebElement disp_txt_cusName;

  @FindBy(css = "#C2__row_ansRowQUE_CD844DE86150F4B31208405 span[class*='beforepipe']")
  WebElement disp_txt_cusDOB;

  @FindBy(css = "tr#C2__p0_TBL_E36EFB44BDE033C53772047>td>div")
  WebElement noInterestedpartiesMsg;

  @FindBy(css = "div#C2__p1_HEAD_E36EFB44BDE033C54659681.inner-title")
  WebElement interestedParties1;

  // @FindBy(css = ".nav.navbar-nav :nth-child(2) a[data-toggle='dropdown'][aria-expanded='false']")
  @FindBy(css = "#C2__C6__ITM_32BF5DA7372009F11482890>a")
  WebElement drpManagePolicy;

  // @FindBy(css = ".midtermadjust")
  @FindBy(css = "#C2__C6__Menu_MTA")
  WebElement lnkMidTermAdjust;

  @FindBy(css = "#C2__C6__p1_QUE_F8495CB9C6ABE93A260881>div>label")
  WebElement labelReverseTransaction;

  @FindBy(css = "#C2__C6__reverse-transaction .modal-dialog .modal-content")
  WebElement mdlReverseTransaction;

  @FindBy(css = "#C2__C6__BUT_F8495CB9C6ABE93A260632")
  WebElement btnConfirmReversal;

  @FindBy(css = ".dropdown-toggle.manageRenewals")
  WebElement drpManageRenewals;

  @FindBy(css = ".inviterenewal")
  WebElement drpoptInviteRenewals;

  @FindBy(css = "#C2__C6__ViewDataCapture")
  WebElement btnWarningContinue;

  @FindBy(css = "#paymentoptions")
  WebElement barPaymentOption;

  @FindBy(css = "#C2__BUT_EE2C7D9B8CC571FB2770393_R1")
  WebElement btnSelectAccountDetails;

  @FindBy(css = "#C2__BUT_D6A69F84D226E30B688852[title='Buy']")
  WebElement btnPaymentBuy;

  @FindBy(css = "#C2__BUT_EE2C7D9B8CC571FB2770393_R1")
  WebElement chkDirectDebit;

  @FindBy(css = "#C2__C6__p1_QUE_F8495CB9C6ABE93A260887>div>label")
  WebElement labelCurrentEffective;

  @FindBy(css = "#C2__C6__p1_QUE_F8495CB9C6ABE93A260884>div>label")
  WebElement labelCurrentExpiry;

  @FindBy(css = "#C2__C6__p1_QUE_F8495CB9C6ABE93A260890>div>label")
  WebElement labelTypeReverseTransaction;

  @FindBy(css = "#C2__C6__BUT_F8495CB9C6ABE93A260632")
  WebElement btnConfirmReverseTransaction;

  @FindBy(css = "#C2__C6__TBL_19504DF73EA7BBF571855 .run-inner-rule-transact.odd :nth-child(2)")
  WebElement textMTAStatus;

  @FindBy(css = "#C2__C6__FMT_F8495CB9C6ABE93A260568 #closeModal")
  WebElement btnRevTransacClose;

  @FindBy(css = "#C2__C6__BUT_F8495CB9C6ABE93A260635")
  WebElement btnCancelReverseTransaction;

  // @FindBy(css = ".reverseTransaction")
  @FindBy(css = "#C2__C6__MenuReverseTransaction")
  WebElement lnkReverseTransaction;

  @FindBy(css = "#C2__C6__QUE_4F6C7E1EA3A0DE0E1111397")
  WebElement txtMidTermDate;

  @FindBy(css = "#C2__C6__QUE_4F6C7E1EA3A0DE0E1111400")
  WebElement cmbMidTermAdjReason;

  @FindBy(css = "#C2__C6__BUT_4F6C7E1EA3A0DE0E1111409")
  WebElement btnMidTermContinue;

  // @FindBy(css = "button.btn.btn-default#C2__BUT_29E0D21D09DB6812743210")
  @FindBy(css = "#C2__BUT_88B187603655E68A1062587")
  WebElement addInterestsearchBtn;

  @FindBy(css = "#C2__QUE_88B187603655E68A1062346")
  WebElement addInterestcontactType;

  @FindBy(css = "#C2__QUE_88B187603655E68A1062352")
  WebElement addInterestFirstname;


  @FindBy(css = "#C2__QUE_88B187603655E68A1062349")
  WebElement addInterestLastname;

  // @FindBy(css = "#C2__BUT_88B187603655E68A1062587")
  @FindBy(css = "#C2__BUT_9233FAA81DFED7AF1370585_R1")
  WebElement addInterestselecthBtn;

  @FindBy(
      css = "select[name='C2__CONTACTCENTRE[1].GETQUOTE[1].POLICYCONTACTS[1].INTERESTEDPARTYOVERLAY[1].INTERESTEDPARTYTYPE']")
  WebElement addInterestpartytype;

  @FindBy(css = "input#C2__QUE_29E0D21D09DB6812718814")
  WebElement addInterestpartytypeTodate;

  // @FindBy(css = "button#C2__BUT_29E0D21D09DB68121013768")
  @FindBy(css = "#C2__BUT_97B9555F366D250C1136871")
  WebElement addInterestpartySave;

  @FindBy(css = "button#C2__BUT_E36EFB44BDE033C54248971_R1.trash")
  WebElement addPartyDeleteicon;

  @FindBy(css = "#C2__C6__QUE_99ADFDA008E513D23696576")
  WebElement cmbCancelPolicyReason;

  @FindBy(css = "#C2__C6__BUT_99ADFDA008E513D23696578")
  WebElement btnCancelPolicyCalculate;

  @FindBy(css = "#C2__C6__QUE_99ADFDA008E513D23844242")
  WebElement cmbCancelPolicyReturn;

  @FindBy(css = "h4.modal-title")
  WebElement deletepopupmodal;

  @FindBy(css = ".Text.sorting_1 span")
  List<WebElement> textMTAStatusEven;

  @FindBy(css = "#C2__C6__p4_QUE_24F4D09ABF5D5E60316278 .col-sm-12.noPadLft")
  WebElement txtPolicyPremiumAmount;

  @FindBy(css = ".reverseTransaction.disable_attr")
  WebElement lnkReverseTransactionDisabled;

  @FindBy(css = "#C2__QUE_728A190A046539421871279")
  WebElement cmbGardenCoverLimit;

  @FindBy(css = "#C2__C6__QUE_F8495CB9C6ABE93A260887")
  WebElement txtRevTransactionEffDate;

  @FindBy(css = "#C2__C6__QUE_F8495CB9C6ABE93A260884")
  WebElement txtRevTransactionExpDate;

  @FindBy(css = "#C2__C6__QUE_F8495CB9C6ABE93A260881")
  WebElement textRevTransactionCurrPosition;

  @FindBy(css = ".table.table-hover.table-striped a[data-target='#C2__content-4']")
  WebElement btnGardenCoverAdd;

  @FindBy(css = "#C2__BUT_728A190A046539421871282")
  WebElement btnGardenCoverSave;

  @FindBy(css = ".stdcancelation")
  WebElement drpoptStandardCancel;

  @FindBy(css = "#C2__QUE_18AB2B9ACA2210FF2182631")
  WebElement standardOrExtraDropdown;

  @FindBy(css = "#C2__BUT_6F266DB023A12C1F473731")
  WebElement switchToButton;

  @FindBy(css = "#C2__C6__QUE_8D734F2AC91F8FEB325795")
  WebElement txtCancelPolicyDate;

  @FindBy(css = ".nav.navbar-nav :nth-child(5) a[data-toggle='dropdown'][aria-expanded='false']")
  WebElement drpCancelPolicy;

  @FindBy(css = "#C2__BUT_E36EFB44BDE033C56411460")
  WebElement yesbtn;

  @FindBy(css = "#C2__BUT_E36EFB44BDE033C56411466")
  WebElement nobtn;

  @FindBy(css = "#C2__FMT_E36EFB44BDE033C55529631>div>button#closeModal.close")
  WebElement closebtn;

  @FindBy(css = "#C2__BUT_E36EFB44BDE033C510171715")
  WebElement addInterestcreateAddInterest;

  @FindBy(css = "#C2__BUT_728A190A046539422616667")
  WebElement btnClickQuoteSave;

  @FindBy(css = "#C2__BUT_40EFD4B99675DBAB1287354")
  WebElement btnContinueSaveQuote;

  @FindBy(css = "#C2__QUE_40EFD4B99675DBAB1256621[name*='EMAIL']")
  WebElement fldEmailSaveQuote;

  @FindBy(css = "button[id*='BUT_29E0D21D09DB6812621225'][title='Cancel']")
  WebElement but_addIntParty_Cancel;

  /**************/
  @FindBy(css = "ul.nav.nav-tabs>li>a")
  WebElement Ele_engagement_Centre_tabs;

  @FindBy(css = "div.main-brand")
  WebElement app_BrandName;

  @FindBy(css = "a[data-target*='personal-details']")
  WebElement enga_Centre_tabs_Personal;

  @FindBy(css = "a[data-target='#C2__C1__corporate-details']")
  WebElement enga_Centre_tabs_Corporate;

  @FindBy(css = "select[name*='PERSONAL'][name*='TITLE']")
  WebElement selecttitle;

  @FindBy(css = "input[name*='PERSONAL'][name*='FIRSTNAME']")
  WebElement cn_firstName;

  @FindBy(css = "input[name*='PERSONAL'][name*='LASTNAME']")
  WebElement cn_lastName;

  @FindBy(css = "input[id*='B5F37DDFDCE76BB21217326']")
  WebElement cn_dateofbirth;

  @FindBy(css = "input[name*='PERSONAL'][name*='POSTCODE")
  WebElement cn_postCode;

  @FindBy(css = "select[name*='PERSONAL'][name*='ADDRESSLIST")
  WebElement cn_AddressList;

  @FindBy(css = "input[name*='PERSONAL'][name*='EMAILADDRESS")
  WebElement cn_Email;

  @FindBy(css = "input[name*='PERSONAL'][name*='SELECTEDPOSTCODE")
  WebElement cn_AddressLine;

  @FindBy(css = "button[id*='B5F37DDFDCE76BB21217366'][title='Find Address']")
  WebElement cn_findAddress;

  @FindBy(css = "input[name*='PERSONAL'][name*='HOMENUMBER")
  WebElement cn_HomePhone;

  @FindBy(css = "div.main-brand")
  WebElement txtBannerName;

  @FindBy(css = "button[id*='SaveBtn_Create_New_Personal_Contact']")
  WebElement cn_SaveCustomer;

  @FindBy(css = "button[id*='SaveBtn_Create_New_Corporate_Contact']")
  WebElement cn_SaveCustomer_Corp;

  @FindBy(css = "button[id*='DIRECT_TAKE_PAYMEN']")
  WebElement but_takPayment;

  @FindBy(css = "input[name*='CARDDETAILS[1].PAYMENTREF']")
  WebElement txtFld_paymentRef;

  @FindBy(css = "#C2__QUE_29E0D21D09DB6812718814_ERRORMESSAGE")
  WebElement txt_errMsg_noDate;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSNAME']")
  WebElement txtTradeName;

  @FindBy(css = "input[name*='CORPORATE[1].TRADINGAS']")
  WebElement txtTradeAs;

  @FindBy(css = "select[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].TYPE")
  WebElement selectType;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].BUILDINGNUMBER']")
  WebElement txtBuldNumber;

  @FindBy(css = "select[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].COUNTRY']")
  WebElement selectCountry;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].POSTCODE']")
  WebElement txtPostcode;

  @FindBy(css = "button[id*='BUT_B5F37DDFDCE76BB21217422'][title='Find Address']")
  WebElement btnfindAddress;

  @FindBy(css = "select[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSADDRESS[1].ADDRESSLIST")
  WebElement AddressList;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESTELEPHONE[1].BUSINESS']")
  WebElement txtBTBiz;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESTELEPHONE[1].HOME']")
  WebElement txtBTHome;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESTELEPHONE[1].MOBILE']")
  WebElement txtBTMob;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSEMAIL[1].EMAIL']")
  WebElement txtBEEmail;

  @FindBy(css = "input[name*='CORPORATE[1].BUSINESSDETAILS[1].BUSINESSEMAIL[1].WEBSITEADDRESS']")
  WebElement txtBEWbsite;

  @FindBy(
      css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].AUTHORISEDTELEPHONENUMBERS[1].HOME']")
  WebElement txtAuthphoneHome;

  @FindBy(
      css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].AUTHORISEDTELEPHONENUMBERS[1].WORK']")
  WebElement txtAuthphonework;
  @FindBy(
      css = "input[name*='.CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].AUTHORISEDTELEPHONENUMBERS[1].MOBILE']")
  WebElement txtAuthphoneMob;
  @FindBy(
      css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].AUTHORISEDTELEPHONENUMBERS[1].EMAIL']")
  WebElement txtAuthphoneEmail;

  @FindBy(css = "select[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].TITLE']")
  WebElement selectTitle;

  @FindBy(css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].FIRSTNAME']")
  WebElement txtFirstName;

  @FindBy(css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].LASTNAME']")
  WebElement txtLastName;

  @FindBy(css = "input[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].MIDDLENAME']")
  WebElement txtMiddleName;

  @FindBy(css = "select[name*='CORPORATE[1].AUTHORISEDCONTACTDETAILS[1].SUFFIX']")
  WebElement selectSuffix;

  @FindBy(css = "label[for='C2__C1__QUE_B5F37DDFDCE76BB21217496_0']")
  WebElement OptMarketing;

  @FindBy(css = "button[id*='SaveBtn_Create_New_Corporate_Contact']")
  WebElement btnSaveCustomer;

  @FindBy(css = "table[summary='PoliciesAndQuotes'] tbody tr span")
  List<WebElement> searchResult;

  // @FindBy(css = "#C2__QUE_C6ED9305165D91DC2171257_R1")
  @FindBy(css = "#C2__QUE_E36EFB44BDE033C53772061_R1")
  WebElement addedParty;

  @FindBy(css = "#C2__BUT_29E0D21D09DB6812621225")
  WebElement but_IP_cancel;

  @FindBy(css = "button[id*='DIRECT_TAKE_PAYMENT']")
  WebElement btnTakePayment;

  @FindBy(css = "input[name*='PAYMENTREF']")
  WebElement paymentRef;

  @FindBy(css = "div>#C2__FMT_E36EFB44BDE033C56426560")
  WebElement wndJointPolicyHolder;

  @FindBy(css = "#C2__ADD_POLICYHOLDER")
  WebElement btnAddPolicyHolder;

  @FindBy(css = "#C2__QUE_88B187603655E68A1062346")
  WebElement selectJointPolicyContactType;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].GETQUOTE[1].POLICYCONTACTS[1].INTERESTEDPARTYOVERLAY[1].SEARCHFORCONTACT']")
  WebElement txtJointPolicyHolderLastName;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].GETQUOTE[1].POLICYCONTACTS[1].INTERESTEDPARTYOVERLAY[1].SEARCHCONTACTSECONDNAME']")
  WebElement txtJointPolicyHolderFirstName;

  @FindBy(css = "#dp1541483963401>input")
  WebElement calJointPolicyHolderDob;

  @FindBy(css = "input[id*='QUE_9233FAA81DFED7AF1007844']")
  WebElement txtJointPolicyHolderPostCode;

  @FindBy(css = "button[title='Search']")
  WebElement btnJointPolicyHolderSearch;

  @FindBy(css = "td[id*='BUT_9233FAA81DFED7AF1371537_R1'] button[title*='Select']")
  WebElement btnJointPolicyHolderSelect;

  @FindBy(css = "button[title='Yes']")
  WebElement btnYesToRemovejointPolicy;

  @FindBy(css = "button[id='C2__BUT_C6ED9305165D91DC3345140']")
  WebElement btnNoToRemovejointPolicy;

  @FindBy(css = "#C2__FMT_C6ED9305165D91DC3220130")
  WebElement wndRemovePolicyPopup;

  @FindBy(css = "table[summary='SearchResultTable'] tbody tr")
  List<WebElement> lstJointPolicySearchResult;

  @FindBy(css = "#C2__QUE_8C541316E9403B341256922_ERRORMESSAGE")
  WebElement errLastNameErrorMsg;

  @FindBy(css = "#C2__QUE_8C541316E9403B341125515_ERRORMESSAGE")
  WebElement errFirstNameErrorMsg;

  @FindBy(css = "#C2__QUE_8C541316E9403B341185332_ERRORMESSAGE")
  WebElement errDobErrorMsg;

  @FindBy(css = "#C2__QUE_8C541316E9403B341209142_ERRORMESSAGE")
  WebElement errPostcodeErrorMsg;

  @FindBy(css = "#C2__BUT_88B187603655E68A1064276")
  WebElement btnCreateNewJointPolicyholder;

  @FindBy(css = "#C2__C1__FMT_05DBD1E701916C229364991")
  WebElement wndCreateNewJointPolicyHolder;

  @FindBy(css = "button#C2__BUT_14E03EE0D08E416C726298")
  WebElement btn_quoteSave;

  @FindBy(css = "[id*='C2__QUE_65369060E37BB4AE3541860_0_R1']")
  WebElement firstMainCheckbox;

  @FindBy(css = "table[summary='PolicyHolderDetails']")
  WebElement tablePolicyholder;

  @FindBy(css = "#C2__p1_QUE_C6ED9305165D91DC2171257")
  WebElement headerContact;

  @FindBy(css = "#C2__p1_QUE_65369060E37BB4AE3541860")
  WebElement headerMain;

  @FindBy(css = "#C2__p1_BUT_C6ED9305165D91DC2171263")
  WebElement headerbinIcon;

  @FindBy(css = "table[summary='PolicyHolderDetails'] tbody tr")
  List<WebElement> lstPolicyHeaderRows;

  @FindBy(css = "button[class='trash']")
  WebElement binIcon;

  @FindBy(css = "#C2__FMT_366A48DA3AD803562879170")
  WebElement policyholdersection;

  @FindBy(css = "#C2__ChangeMainPolicyholder_Yes")
  WebElement btnYesToChangeMainCheckbox;

  @FindBy(css = "#C2__ChangeMainPolicyholder_No")
  WebElement btnNoToChangeMainCheckbox;

  @FindBy(css = "#pmitem_0 > a")
  WebElement lnkVisa;

  @FindBy(css = "iframe[id='wp-cl-custom-html-iframe']")
  WebElement frame_sagepay;

  @FindBy(css = "#C2__QUE_2411741C919DF0934660562")
  WebElement txt_paymntRef;

  @FindBy(css = "#C2__BUT_14E03EE0D08E416C726298")
  WebElement btnSave;

  @FindBy(css = "#C2__p4_QUE_E36EFB44BDE033C53772061_R1 span")
  WebElement txtFld_IntPartyCustName;

  @FindBy(css = "#C2__TXT_728A190A046539421305882")
  WebElement txt_AddCoverLE;

  @FindBy(css = "#C2__TXT_728A190A046539421309389")
  WebElement txt_AddCoverHE;

  @FindBy(css = "#C2__TXT_728A190A046539421302905 h4")
  WebElement txt_addCover;

  @FindBy(css = "select[name*='COMPONENT']")
  WebElement dropdownComponent;

  @FindBy(css = "[id*='C1__TXT_0FF02AFB3DFE5A53595462']")
  WebElement msgMandatoryTandC;

  @FindBy(css = "[id*='C2__C1__QUE_13B992799BD80BAE1217217_R1_1']")
  WebElement MandatoryTandC;

  @FindBy(css = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_1']")
  WebElement rowMandatoryTandC;

  @FindBy(css = "button[value='Accept']:not([disabled='disabled'])")
  WebElement btnAcceptPayment;

  @FindBy(css = "#C2__BUT_D6A69F84D226E30B688852[title='Buy']")
  WebElement rnlbtnAcceptPayment;

  @FindBy(css = "button[id$='BUT_7499E8C9DD09DCA41348358']")
  WebElement btnCancelPayment;

  @FindBy(css = "button[id$='BUT_FD051A198CB3861E3809030']")
  WebElement btnCancelPaymentYes;

  @FindBy(css = "#C2__BUT_EE2C7D9B8CC571FB2770393_R1")
  WebElement btnAcctSelect;

  @FindBy(css = "button[title='Add Account Details']")
  WebElement btnAddAccDetail;

  @FindBy(css = "input[id='C2__QUE_EE2C7D9B8CC571FB1355397']")
  WebElement txtAccNumber;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].SORTCODE']")
  WebElement txtAccSortCode;

  @FindBy(css = "button[title='Check Account']")
  WebElement btnCheckAccount;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].BRANCH_READONLY']")
  WebElement txtAccBranch;

  @FindBy(css = "input[name*='ACCOUNTDETAILS[1].ACCOUNTNAME']")
  WebElement txtAccName;

  @FindBy(css = "button[id='C2__ID_DIRECT_DEBIT_SAVE']")
  WebElement btnAddAccSave;

  @FindBy(css = "div[id$='TXT_FB5B4FF08C6963561542971'] div.payorErrorMsg div.col-md-10")
  WebElement divAddAccDetErrMsg;

  @FindBy(css = "div[id$='TXT_BBB7F11DB6E1002D847240'] div.payorErrorMsg div.col-md-10")
  WebElement divAccDetErrMsg;

  @FindBy(css = "#C2__HEAD_EE2C7D9B8CC571FB2770397")
  WebElement divNoAccDetErrMsg;

  @FindBy(css = "div#C2__p4_QUE_E2BF6E7D321C01D51456374>div>fieldset>div>input[type='checkbox']")
  WebElement chkCustAgree;

  @FindBy(css = "[name*='PREFERREDPAYMENTDAY']")
  WebElement selectPreferredPaymentDay;

  @FindBy(css = "div[id*='GRP_9C281AC77BF4F72E2110808'] div.table_foot div:nth-child(1)")
  WebElement divTotalPremium;

  @FindBy(css = "div[id*='GRP_9C281AC77BF4F72E2110808'] div.table_foot div:nth-child(2)")
  WebElement divTotalCalculatedPremium;

  @FindBy(css = "div[id*='GRP_9C281AC77BF4F72E2110808'] div.table_foot div:nth-child(3)")
  WebElement divTotalTaxes;

  @FindBy(css = "div[id*='GRP_9C281AC77BF4F72E2110808'] div.table_foot div:nth-child(4)")
  WebElement divTotalCharges;

  @FindBy(css = "div[id*='GRP_9C281AC77BF4F72E2110808'] div.table_foot div:nth-child(5)")
  WebElement divTotalChargesTax;

  @FindBy(css = "div[id*='GRP_9C281AC77BF4F72E2110808'] div.table_foot div:nth-child(6)")
  WebElement divTotalInstallments;

  @FindBy(css = "div[id*='GRP_9C281AC77BF4F72E2110808'] div.table_foot div:nth-child(7)")
  WebElement divTotalAmountPayable;

  @FindBy(css = "div[id='C2__TXT_728A190A04653942818940'] div span")
  WebElement spnOneOffPaymentCoverTab;

  @FindBy(css = "div[id='C2__row_TXT_728A190A04653942814272'] div span")
  WebElement spnTotalPaymentCoverTab;

  @FindBy(css = "div[id='C2__TXT_728A190A04653942815447'] div div")
  WebElement spnFirstPaymentCoverTab;

  @FindBy(css = "div[id='C2__TXT_728A190A04653942816020'] div div")
  WebElement spnInstallmentPaymentCoverTab;

  @FindBy(css = "span[id='C2__QUE_1296CBF41B899BC32592888']")
  WebElement spnOneOffPaymentAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_24D10CAD2CBD74854948763']")
  WebElement spnTotalPaymentAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_24D10CAD2CBD74854948769']")
  WebElement spnFirstPaymentAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_24D10CAD2CBD74854948771']")
  WebElement spnInstallmentPaymentAcceptanceTab;

  @FindBy(css = "div[id='C2__row_QUE_24D10CAD2CBD74854948775']")
  WebElement divIncludesFee1;

  @FindBy(css = "div[id='C2__row_QUE_24D10CAD2CBD74854948809']")
  WebElement divIncludesFee2;

  @FindBy(css = "#C2__C2__BUT_CE93A4FCF8469E1D5834607[title='Change Payor']")
  WebElement btnChangePayor;

  @FindBy(css = ".modal-content.Payor #closeModal")
  WebElement btnCloseChangePayor;

  @FindBy(css = ".modal-content.Payor [title='Cancel']")
  WebElement btnCancelChangePayor;

  @FindBy(css = "#C2__C2__New_change_payer:not([style*='none'])")
  WebElement mdlChangePayorOpen;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5834594[name*='PAYOR']")
  WebElement fldPayor;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5823072")
  WebElement fldPayorLastName;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5823074")
  WebElement fldPayorFirstName;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5823076")
  WebElement fldPayorDOB;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5823078")
  WebElement fldPayorPostCode;

  @FindBy(css = "button[id*='Payor_Search']")
  WebElement btnPayorSearch;

  @FindBy(css = "#C2__C2__p1_QUE_E0D434CA692481AD1202231>div>label>p")
  WebElement btnPayorGetName;

  @FindBy(css = "tr[id*='R1'] button")
  WebElement btnPayorSearchSelectRow1;

  @FindBy(css = "button[class*='payor-confirm']")
  WebElement btnPayorConfirm;

  @FindBy(css = "input[id*='QUE_D44A4DCA8AF3342E1982539']")
  WebElement chkSpokenDirectlyNewPayor;

  @FindBy(css = ".modal-content.Payor .alert-danger .errorClass")
  WebElement errorMsgPayorMandatoryFields;

  @FindBy(css = "#C2__BUT_41DC0DB7791BD869934726[title='Save']")
  WebElement btn_renewQuoteSave;

  @FindBy(css = "input[name*='QUOTEDESCRIPTION']")
  WebElement txtFld_quoteDesc;

  @FindBy(css = "button[title='SavePricePres']")
  WebElement btn_saveQuoteDesc;

  @FindBy(css = "button#C2__BUT_C1022B81F9580E243978254[title='Cancel']")
  WebElement btn_qutCancel;

  @FindBy(css = "#C2__C1__FMT_9F00A31B5618E9DE526536")
  WebElement mdlConfirmationWnd;

  @FindBy(css = "#C2__C1__BUT_9F00A31B5618E9DE638289")
  WebElement btnYesTandC;

  @FindBy(css = "#C2__C1__BUT_867B666D4B802B0A144515")
  WebElement btnNoTandC;

  @FindBy(css = "#C2__FMT_2411741C919DF0934302708")
  WebElement panelCardDetailSection;

  @FindBy(css = "[title='Clear All']")
  WebElement btnClearAll;

  @FindBy(css = "#C2__C2__p1_HEAD_CE93A4FCF8469E1D5823084")
  WebElement errNoResultFound;

  @FindBy(css = "#C2__C2__BUT_CE93A4FCF8469E1D6218944")
  WebElement btnCreateNewPayor;

  @FindBy(css = "table[summary='SearchResult'] tbody tr")
  List<WebElement> lstChangePayorSearchResult;

  @FindBy(css = "#C2__C2__p1_HEAD_E0D434CA692481AD1022790")
  WebElement msgYouHaveSelect;

  @FindBy(css = "#C2__C2__BUT_CE93A4FCF8469E1D5823098")
  WebElement btnChangePayorCancel;

  @FindBy(css = ".adjustwidth.prog_Active>div>button")
  WebElement datacapturetitle;

  @FindBy(css = ".adjustwidth.prog_Active>div>button")
  WebElement pricePresentationtitle;

  @FindBy(css = ".adjustwidth.prog_Active>div>button")
  WebElement accptancetitle;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].YOURDETAILS[1].HOME[1].COVERSTARTDATE_READONLY'][readonly='readonly]")
  WebElement coverStartDateonDC;

  @FindBy(css = "#C2__BUT_728A190A046539422616661")
  WebElement btnRnlBacktoDashboard;

  @FindBy(css = "#C2__C2__C1__FMT_05DBD1E701916C229364991")
  WebElement mdlCreateNewPayor;

  @FindBy(css = "#C2__C2__C1__SaveBtn_Create_New_Personal_Contact")
  WebElement btnSaveCreateNewPayor;

  @FindBy(css = "#C2__TXT_2411741C919DF0934326893")
  WebElement msgNoCardDetail;

  @FindBy(css = "h1")
  WebElement paymentHub_title;

  @FindBy(css = "#C2__QUE_2411741C919DF0934660562")
  WebElement paymentReferrence;

  @FindBy(css = "#C2__C2__BUT_CE93A4FCF8469E1D6218944")
  WebElement newPayorButton;

  @FindBy(css = "#C2__C2__C1__row_QUE_B5F37DDFDCE76BB21217316")
  WebElement titleFieldInNewPayor;

  @FindBy(css = "#C2__C2__C1__p1_QUE_B5F37DDFDCE76BB21217318")
  WebElement firstNameFieldInNewPayor;

  @FindBy(css = "#C2__C2__C1__row_QUE_B5F37DDFDCE76BB21217322")
  WebElement lastNameFieldInNewPayor;

  @FindBy(css = "#C2__C2__C1__HEAD_B5F37DDFDCE76BB21217358")
  WebElement addressFieldInNewPayor;

  @FindBy(css = ".custom-close")
  WebElement crossButton;

  @FindBy(css = "#C2__C2__row_BUT_CE93A4FCF8469E1D5823070")
  WebElement searchScreen;

  @FindBy(css = "#C2__C2__QUE_D44A4DCA8AF3342E1982539_0")
  WebElement checkBox;

  @FindBy(css = "#C2__C2__QUE_CE93A4FCF8469E1D5823088_R")
  List<WebElement> lstSearchPayor;

  @FindBy(css = "#C2__C2__FMT_ADB489001C287B4F2339481")
  WebElement searchResults;

  @FindBy(css = ".ModalTitle")
  WebElement newPayorPage;

  @FindBy(css = "table[id*='C2__C2__TBL_CE93A4FCF8469E1D5823086'] tbody tr")
  List<WebElement> lstsearchpayor;

  @FindBy(css = "#C2__HEAD_EE2C7D9B8CC571FB2770397")
  WebElement msgNoAccountDetail;

  @FindBy(css = "#C2__HEAD_EE2C7D9B8CC571FB2596088")
  WebElement h1AccDetailsTitle;

  @FindBy(css = "table[summary='BankDetails'] tbody tr")
  List<WebElement> lstAccountDetail;

  @FindBy(css = "input[placeholder='Start Date'][type='text']")
  WebElement datePicker;

  @FindBy(css = "	#C2__FMT_9AF9ECE9A7A9E74D1005531")
  WebElement mdlNewQuote;

  @FindBy(css = "#C2__TXT_27AD7ED00DE1530E817951")
  WebElement errJointPolicyErrorMsg;

  @FindBy(css = "button[id='BUT_925B6D15BDB617A8717552']")
  WebElement btnConfirmMTAPaymet;
  @FindBy(css = "button[id*='BUT_88B187603655E68A1064276']")
  WebElement addInterestcreateAddInterestbutton;

  /**
   * 
   * Constructor class for Customer Dashboard Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */
  public NewQuotePage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
    uielement = new ElementLayer(driver);
  }

  @Override
  protected void isLoaded() {

    WaitUtils.waitForPageLoad(driver);
    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("NewQuote")) {
      Log.fail("User is not navigated to New Quote Page", driver, extentedReport);
    }
  }

  @Override
  protected void load() {

    isPageLoaded = true;

  }

  /**
   * Verify NewQuotePage
   * 
   * @param Screenshot
   * @throws Exception : Custom Exception Message
   * @param extentReport
   */
  public boolean verifyNewQuotePage(boolean Screenshot, ExtentTest extentReport) throws Exception {
    boolean status = false;
    if (WaitUtils.waitForElement(driver, lnkBacktoDashboard)) {
      status = true;
      Log.message("Navigated to New Quote page", driver, extentReport, Screenshot);
    }
    return status;
  }

  /**
   * Enter Policy Details
   * 
   * @param testdata : Hashmap
   * @param screenshot :boolean
   * @param extentReport
   * @throws Exception
   * 
   */
  public void enterPolicyDetails(HashMap<String, String> testdata, boolean Screenshot,
      ExtentTest extentReport) throws Exception {

    try {
      Log.event("Entering Policy Details");
      selectCover(testdata.get("Cover").toString());
      Log.message("Cover Chosen : " + testdata.get("Cover").toString(), extentReport);
      Thread.sleep(3000);
      selectNumberofBedrooms(testdata.get("No of Bedrooms").toString());
      Log.message("No of bedrooms :" + testdata.get("No of Bedrooms").toString(), extentReport);
      WaitUtils.waitForElement(driver, txtYearofBuild);
      txtYearofBuild.click();
      txtYearofBuild.sendKeys(testdata.get("YOB").toString());
      Log.message("Year of Build : " + testdata.get("YOB").toString(), extentReport);
      txtYearofBuild.click();
      // txtYearofBuild.sendKeys(Keys.TAB);
      selectPropertyType(testdata.get("Property Type").toString());
      Thread.sleep(3000);
      Log.message("Property Type chosen : " + testdata.get("Property Type").toString(),
          extentReport);
      Log.message("Successfully entered Policy details", driver, extentReport, Screenshot);

    } catch (Exception e) {
      throw new Exception("Unable to Enter Policy Details : " + e);
    }
  }

  /**
   * click Next One button
   * 
   * @param extentReport
   *
   * @throws Exception : Custom Exception Message
   */
  public void clickNextOne(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, btnNextOne, "Unable to Next Button");
      btnNextOne.click();
      Log.message("Clicked on Next button", extentReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Next button : " + e);
    }
  }

  /**
   * Entering Date Of Birth in New Quote Page
   * 
   * @param extendReport
   * @param dateOfBirth
   * @throws Exception : Custom Exception Message
   */

  public void enterDobNewQuote(String dateOfBirth, ExtentTest extendReport) throws Exception {
    try {
      dateOfBirthNewQuote.clear();
      dateOfBirthNewQuote.click();
      dateOfBirthNewQuote.sendKeys(dateOfBirth);
      Thread.sleep(2000);
      Log.message("Entered the Date of Birth: " + dateOfBirth, driver, extendReport);
    }

    catch (Exception e) {
      throw new Exception("Date of Birth is not entered" + e);
    }
  }

  /**
   * Enter CustomerDetails
   * 
   * @param testdata : Hashmap
   * @param screenshot :boolean
   * @param Corecover :String
   * @param extentReport
   * @throws Exception
   * 
   */
  public void enterCustomerDetails(HashMap<String, String> testdata, boolean Screenshot,
      String Corecover, ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      if ((!Corecover.equals("Contents")) && (!Corecover.equals("Buildings"))) {
        Log.message("Entering customer details for " + Corecover + " corecover", extentReport);
        selectbuildIns(testdata.get("Content/Building Ins").toString());
        selectContIns(testdata.get("Content/Building Ins").toString());
        selectClaims(testdata.get("Claims").toString());
        selectPersonal(testdata.get("Personal Items").toString());
        selectBicyle(testdata.get("Bicycle").toString());
        selectHighrisk(testdata.get("High Risk").toString());
      } else if (Corecover.equals("Contents")) {
        Log.message("Entering customer details for " + Corecover + " corecover", extentReport);
        selectContIns(testdata.get("Content/Building Ins").toString());
        selectClaims(testdata.get("Claims").toString());
        selectPersonal(testdata.get("Personal Items").toString());
        selectBicyle(testdata.get("Bicycle").toString());
        selectHighrisk(testdata.get("High Risk").toString());
      } else {
        Log.message("Entering customer details for " + Corecover + " corecover", extentReport);
        testdata.get("Date of Birth").toString();
        // enterDobNewQuote(dateOfBirth, extentReport);
        selectbuildIns(testdata.get("Content/Building Ins").toString());
        selectClaims(testdata.get("Claims").toString());

      }

    } catch (Exception e) {
      throw new Exception("Unable to Enter Customer Details :" + e);
    }
  }

  /**
   * click NextTwo button
   * 
   * @param extentReport
   * @throws Exception : Custom Exception Message
   */
  public void clickNextTwo(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      btnNextTwo.click();
      Log.message("Clicked on Next button", extentReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Next" + e);
    }
  }

  /**
   * click View button
   * 
   * @param extentReport
   * @throws Exception : Custom Exception Message
   */
  public void clickView(ExtentTest extentReport) throws Exception {
    try {

      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 4000).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find view button"))
              .until(ExpectedConditions.elementToBeClickable(btnView));
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", btnView);
      // btnView.click();
      WaitUtils.waitForPageLoad(driver);
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on View Button", driver, extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to Click on View Button :" + e);
    }
  }

  /**
   * click Agree
   * 
   * @throws Exception : Custom Exception Message
   * @param extentReport
   */
  public void clickAgree(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, btnSaveDeclaration);
      selectAgree("Customer agrees", extentReport);
      btnSaveDeclaration.click();

      Thread.sleep(3000);
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Save Button", extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to Click on Agreement Statement : " + e);
    }
  }

  /**
   * click GetQuote
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */
  public void clickGetQuote(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnGetQuote);
      btnGetQuote.click();
      Log.message("Clicked on Get Quote button sucessfully", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForPageLoad(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on GetQuote Button : " + e);
    }
  }

  /**
   * click Buy button
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */

  public void clickBuy(ExtentTest extentedReport) throws Exception {
    try {
      Thread.sleep(2000);
      WaitUtils.waitForElement(driver, btnBuy);
      try {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnBuy);
      } catch (Exception e) {
        btnBuy.click();
      }
      Log.message("Clicked on Buy Quote button", driver, extentedReport, true);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Buy Button - Premium values could be zero :" + e);
    }
  }

  /**
   * click Renewal Quote Buy button
   * 
   * @param extentedReport
   * @param buyType
   * @throws Exception : Custom Exception Message
   */
  public void clickRenewalQuoteBuy(ExtentTest extentedReport, String... buyType) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnRenewalQuoteBuy);
      btnRenewalQuoteBuy.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Renewal Quote Buy button", driver, extentedReport, true);

    } catch (Exception e) {
      throw new Exception(
          "Unable to Click on Renewal Buy Button - Premium values could be zero :" + e);
    }
  }

  /**
   * click Preference Next button
   * 
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   */
  public void clickCustomerPreferenceNext(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnCustomerPreferenceNext);
      btnCustomerPreferenceNext.click();
      Log.message("Clicked Payment next button", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Payment Next Button" + e);
    }
  }

  /**
   * Get payment method
   * 
   * @throws Exception
   * @return String
   * 
   */

  public String getPaymentMethod() throws Exception {
    return GenericUtils.getTextOfWebElement(selectpaymentmethod, driver);

  }

  /**
   * Verify already stored bank details are listed
   * 
   * @param branchName
   * @param testdata
   * @param Screenshot
   * @param extentedReport
   * @return boolean
   * 
   * 
   */

  public boolean verifyStoredBankDetailsListed(String branchName, HashMap<String, String> testdata,
      boolean Screenshot, ExtentTest extentedReport) {
    boolean status = false;
    WaitUtils.waitForElementPresent(driver, but_AddAccDetail,
        "Add Account detail button is not found");
    but_AddAccDetail.click();
    WaitUtils.waitForElementPresent(driver, txt_AccNumber, " Account number textbox is not found");

    txt_AccNumber.clear();
    txt_AccNumber.sendKeys(testdata.get("Account Number").toString());
    Log.message("Account Number entered :" + testdata.get("Account Number"), extentedReport);

    txt_SortCode.clear();
    txt_SortCode.sendKeys(testdata.get("Sort Code").toString());
    Log.message("Sort code entered :" + testdata.get("Sort Code"), extentedReport);

    WaitUtils.waitForElementPresent(driver, but_CheckAccount, "Check account button is not found");
    but_CheckAccount.click();

    WaitUtils.waitForSpinner(driver);

    if (fldBranch.getAttribute("value").equalsIgnoreCase(branchName))
      status = true;
    but_AddAccSave.click();
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked on account save button", extentedReport);
    if (tableAccDetailsBranch.getText().equals(branchName))
      status = true;
    Log.message("Account Branch Name is :" + tableAccDetailsBranch.getText(), driver,
        extentedReport, Screenshot);
    return status;

  }

  /**
   * click terms & condition
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */
  public void clickTerms(ExtentTest extentedReport) throws Exception {
    try {

      try {
        Thread.sleep(2000);
        WaitUtils.waitForElementPresent(driver, termsconditions,
            "Failed to locate Terms and condition check box");
        termsconditions.click();
      } catch (Exception f) {
        f.printStackTrace();
      }
      WaitUtils.waitForElementPresent(driver, btnPaymentNexttwo,
          "Failed to locate next button after clicking on terms and condition checkbox");
      btnPaymentNexttwo.click();
      Log.message("Selected Terms and Conditions and click next button sucessfully", driver,
          extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Terms and Conditions" + e);
    }
  }

  /**
   * Description : click TermsAndCondition Section in acceptance page
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   */
  public void clickTermsAndConditionSection(ExtentTest extentedReport) throws Exception {
    try {

      WaitUtils.waitForElementPresent(driver, termsAndConditionsSection,
          "Failed to locate Terms and condition check box");
      termsAndConditionsSection.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Terms and Condition Section", extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while clicking termsAndConditions Section" + e);
    }

  }

  /**
   * Description : click TermsAndCondition button of TermsAndCondition section in acceptance page
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   */
  public void clickAttachTermAndConditionButton(ExtentTest extentedReport) throws Exception {
    try {

      WaitUtils.waitForElementPresent(driver, btnAttachTermAndCondition,
          "Failed to locate Attaceh Termsandcondition button");
      btnAttachTermAndCondition.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on button - Attach Term and Condition", extentedReport);
    } catch (Exception e) {
      throw new Exception("Error while clicking Attach TermandCondition button" + e);
    }

  }

  /**
   * Description : Verify First Terms and Conditions
   *
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   * 
   */

  public void addFirstPolicy(ExtentTest extentedReport) throws Exception {
    try {
      if (firstTermAndConditionInList.isDisplayed()) {
        firstTermAndConditionInList.click();
        WaitUtils.waitForSpinner(driver);
        if (!selectTAC.isDisplayed() && !btnEditTermAndCondition.isDisplayed()) {
          firstTermAndConditionInList.click();
          WaitUtils.waitForSpinner(driver);
        }
        WaitUtils.waitForSpinner(driver);
        Log.message("Clicked on first Term and Condition", driver, extentedReport);
      } else {
        throw new Exception("First Term and condition not visible");
      }
    } catch (Exception e) {
      throw new Exception("Unable to read the Terms and Conditions" + e);
    }
  }

  /**
   * Description : Read Terms & Conditions section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyReadTermsAndCondition(ExtentTest extentedReport) throws Exception {
    try {
      clickTermsAndConditionSection(extentedReport);
      clickAttachTermAndConditionButton(extentedReport);
      addFirstPolicy(extentedReport);

      if (selectTAC.getText().length() > 0) {
        Log.message("Terms and Conditions : " + selectTAC.getText(), extentedReport);
        return true;
      } else {
        Log.message("Terms and Conditions : " + selectTAC.getText(), extentedReport);
        return false;
      }
    } catch (Exception e) {
      throw new Exception("Unable to read the Terms and Conditions" + e);
    }
  }

  /**
   * Description : Edit Terms & Conditions section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyEditTermAndCondition(ExtentTest extentedReport) throws Exception {
    boolean isEdited = false;
    try {

      String randomValue = "Testing";
      clickTermsAndConditionSection(extentedReport);

      if (btnAttachTermAndCondition.isDisplayed()) {
        btnAttachTermAndCondition.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Clicked on button - Attach Term and Condition", driver, extentedReport);
        if (firstTermAndConditionInList.isDisplayed()) {
          firstTermAndConditionInList.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on first Term and Condition", driver, extentedReport);
        } else {
          throw new Exception("First Term and condition not visible");
        }
      } else {
        throw new Exception("Button Attach Term and Condition not visible");
      }

      WebElement btnEdit = firstTermAndConditionInList.findElement(By.cssSelector(cssEditButton));
      btnEdit.click();
      WaitUtils.waitForSpinner(driver);

      firstTermAndConditionTextArea.click();

      // Edit the text
      ((JavascriptExecutor) driver).executeScript("arguments[0].innerText='" + randomValue + "'",
          firstTermAndConditionTextArea);

      if (firstTermAndConditionTextArea.getText().contains(randomValue)) {
        isEdited = true;
      }

      return isEdited;
    } catch (Exception e) {
      throw new Exception("Unable to edit the Terms and Conditions" + e);
    }
  }

  /**
   * Fills up Payment section input fields with the given test data and checks the 'Customer agrees
   * ...' checkbox.
   * 
   * @param testData
   * @param Screenshot
   * @param extentedReport
   * @throws Exception
   */
  public void selectPayment(HashMap<String, String> testData, boolean Screenshot,
      ExtentTest extentedReport) throws Exception {
    try {
      String paymentPlan = testData.get("Payment Plan");
      String paymentMethod = testData.get("Payment Method");
      WaitUtils.waitForSpinner(driver);
      selectPaymentPlan(paymentPlan, extentedReport);
      selectPaymentMethod(paymentMethod.toString(), extentedReport);

      if (paymentPlan.equals("Monthly")) {

        String accNo = testData.get("Account Number");
        String sortCode = testData.get("Sort Code");
        String accName = testData.containsKey("Account Name") ? testData.get("Account Name")
            : GenericUtils.getRandomCharacters("alpha", 5);
        String branch =
            (testData.containsKey("Bank Branch") && !testData.get("Bank Branch").equals(""))
                ? testData.get("Bank Branch") : "Bootle Centre, Santander, Liverpool";

        clickAddAccountDetails(extentedReport);
        checkBankAccount(accNo, sortCode, extentedReport);
        enterAccountName(accName, extentedReport);
        saveAccountDetails(extentedReport);
        selectAccount(sortCode, branch, accName, accNo, extentedReport, true);
        verifyAccountSelection(sortCode, branch, accName, accNo, extentedReport);
        tickCustomerAgreesCheckbox(extentedReport);
      }
    } catch (Exception e) {
      throw new Exception("Exception occured,Unable to Select Payment" + e);
    }
  }

  /**
   * Clicks on 'Add Account Details' button.
   * 
   * @param extentedReport
   */
  public void clickAddAccountDetails(ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, btnAddAccDetail,
        "Add Account detail button is not found");
    btnAddAccDetail.click();
    Log.message("Clicked 'Add Account Details' button", extentedReport);
  }

  /**
   * Clicks on 'Check Account' button after writing the given account number and sort code in the
   * 'Account Number' and 'Sort Code' input fields respectively.
   * 
   * @param accountNumber
   * @param sortCode
   * @param extentedReport
   */
  public void checkBankAccount(String accountNumber, String sortCode, ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, txtAccNumber, " Account number textbox is not found");
    txtAccNumber.clear();
    txtAccNumber.sendKeys(accountNumber);
    Log.message("Account number entered : " + accountNumber, extentedReport);

    txtAccSortCode.clear();
    txtAccSortCode.click();
    ((JavascriptExecutor) driver).executeScript(
        "document.getElementById('C2__QUE_EE2C7D9B8CC571FB1355410').value='09-01-26';");

    Log.message("Sort code entered : " + sortCode, extentedReport);

    WaitUtils.waitForElementPresent(driver, btnCheckAccount, "Check account button is not found");
    btnCheckAccount.click();
    WaitUtils.waitForSpinner(driver);
  }

  /**
   * Verifies that the actual error message in 'Add Account Details' matches the expected error
   * message given.
   * 
   * @param expectedErrMsg
   * @param extentedReport
   * @return true if the actual error message matches the expected error message given; false
   *         otherwise.
   */
  public boolean verifyAddAccountDetailsErrorMessage(String expectedErrMsg,
      ExtentTest extentedReport) {
    String actualErrMsg = divAddAccDetErrMsg.getText().trim();
    Log.message("Actual Error Message : '" + actualErrMsg + "'", extentedReport);
    Log.message("Expected Error Message : '" + expectedErrMsg + "'", extentedReport);

    return actualErrMsg.equals(expectedErrMsg);
  }

  /**
   * Verifies that the actual sort code matches the expected sort code given.
   * 
   * @param expectedSortCode
   * @param extentedReport
   * @return true if the actual sort code matches the expected sort code given; false otherwise.
   */
  public boolean verifySortCode(String expectedSortCode, ExtentTest extentedReport) {
    boolean isOk = false;
    String actualSortCode = txtAccSortCode.getAttribute("value").trim();
    Log.message("Actual Sort code: '" + actualSortCode + "'", extentedReport);
    Log.message("Expected Sort code: '" + expectedSortCode + "'", extentedReport);

    if (actualSortCode.equals(expectedSortCode)) {
      isOk = true;
    } else {
      Log.message("The actual sort code '" + actualSortCode + "' does not match the expected '"
          + expectedSortCode + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * Verifies that the actual branch name matches the expected branch name given.<br/>
   * <br/>
   * Also verifies that the 'Branch Name' input field is locked to edit.
   * 
   * @param expectedBranchName
   * @param extentedReport
   * @return true if the actual branch name matches the expected branch name given and the field is
   *         not editable; false otherwise.
   */
  public boolean verifyBranchName(String expectedBranchName, ExtentTest extentedReport) {
    boolean isOk = false;
    String actualBranchName = txtAccBranch.getAttribute("value").trim();
    Log.message("Actual Branch: '" + actualBranchName + "'", extentedReport);
    Log.message("Expected Branch: '" + expectedBranchName + "'", extentedReport);

    if (actualBranchName.equals(expectedBranchName)) {
      if (!txtAccBranch.isEnabled()) {
        isOk = true;
        Log.message("'Branch Name' input field is not editable", extentedReport);
      } else {
        Log.message("'Branch Name' input field is editable", extentedReport);
      }
    } else {
      Log.message("The actual branch name '" + actualBranchName + "' does not match the expected '"
          + expectedBranchName + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * Verifies that the actual account name matches the expected account number given.
   * 
   * @param expectedAccName
   * @param extentedReport
   * @return true if the actual account name matches the expected account name given; false
   *         otherwise.
   */
  public boolean verifyAccountName(String expectedAccName, ExtentTest extentedReport) {
    boolean isOk = false;
    String actualAccName = txtAccName.getAttribute("value").trim();
    Log.message("Actual Acc Name: '" + actualAccName + "'", extentedReport);
    Log.message("Expected Acc Name: '" + expectedAccName + "'", extentedReport);

    if (actualAccName.equals(expectedAccName)) {
      isOk = true;
    } else {
      Log.message("The actual account name '" + actualAccName + "' does not match the expected '"
          + expectedAccName + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * Verifies that the actual account number matches the expected account number given.
   * 
   * @param expectedAccNo
   * @param extentedReport
   * @return true if the actual account number matches the expected account number given; false
   *         otherwise.
   */
  public boolean verifyAccountNumber(String expectedAccNo, ExtentTest extentedReport) {
    boolean isOk = false;
    String actualAccNo = txtAccNumber.getAttribute("value").trim();
    Log.message("Actual Acc No: '" + actualAccNo + "'", extentedReport);
    Log.message("Expected Acc No: '" + expectedAccNo + "'", extentedReport);

    if (actualAccNo.equals(expectedAccNo)) {
      isOk = true;
    } else {
      Log.message("The actual account number '" + actualAccNo + "' does not match the expected '"
          + expectedAccNo + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * Writes the given account name in the 'Account Name' input field.
   * 
   * @param accountName
   * @param extentedReport
   */
  public void enterAccountName(String accountName, ExtentTest extentedReport) {
    txtAccName.clear();
    txtAccName.sendKeys(accountName);
    Log.message("Account name entered : " + accountName, extentedReport);
  }

  /**
   * Clicks on 'Save' button in 'Add Account Details' section.
   * 
   * @param extentedReport
   */
  public void saveAccountDetails(ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, btnAddAccSave,
        "Save button to add account is not found");
    btnAddAccSave.click();
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked on account save button", extentedReport);
  }

  /**
   * Selects the expected account which is already added.
   * 
   * @param sortCode
   * @param branch
   * @param accName
   * @param accNo
   * @param extentedReport
   * @param screenshot
   */
  public void selectAccount(String sortCode, String branch, String accName, String accNo,
      ExtentTest extentedReport, boolean screenshot) throws Exception {
    branch = "Bootle Centre, Santander, Liverpool";
    sortCode = sortCode.matches("\\d{2}-\\d{2}-\\d{2}") ? sortCode
        : sortCode.substring(0, 2) + sortCode.substring(2, 4) + sortCode.substring(4);
    String xpathAccSelect = xpathAccSelectBtnOfAccDetails.replaceAll("\\$\\{SORT_CODE\\}", sortCode)
        .replaceAll("\\$\\{BRANCH\\}", branch).replaceAll("\\$\\{ACC_NAME\\}", accName)
        .replaceAll("\\$\\{ACC_NO\\}", accNo);
    GenericUtils.scrollIntoView(driver, h1AccDetailsTitle);
    WaitUtils.waitForElementPresent(driver, "xpath", xpathAccSelect, 30,
        "Timed out to find the 'select' button of account with following details {sort code: '"
            + sortCode + "', branch: '" + branch + "', account name: '" + accName
            + "', account number: '" + accNo + "'}");
    driver.findElement(By.xpath(xpathAccSelect)).click();
    WaitUtils.waitForSpinner(driver);
    WaitUtils.waitForElementAbsent(driver, "xpath", xpathAccSelect, 30,
        "Timed out waiting for the absence of the 'select' button of account with following details {sort code: '"
            + sortCode + "', branch: '" + branch + "', account name: '" + accName
            + "', account number: '" + accNo + "'}");
    Log.message("Clicked on the select button of account with account name '" + accName + "'",
        driver, extentedReport, true);
  }

  /**
   * Verifies all the fields in the 'Account Details' section and returns the verification status.
   * 
   * @param sortCode
   * @param branch
   * @param accName
   * @param accNo
   * @param extentedReport
   * @return true if all the fields in the 'Account Details' section are successfully verified;
   *         false otherwise.
   */
  public boolean verifyAccountDetails(String sortCode, String branch, String accName, String accNo,
      ExtentTest extentedReport) {
    boolean isOk = false;

    if (verifySortCode(sortCode, extentedReport) && verifyBranchName(branch, extentedReport)
        && verifyAccountName(accName, extentedReport)
        && verifyAccountNumber(accNo, extentedReport)) {
      isOk = true;
    }

    return isOk;
  }

  /**
   * Verifies that the 'Select' button of the selected account is hidden and the 'Account Details'
   * fields are populated with the selected account details.
   * 
   * @param sortCode
   * @param branch
   * @param accName
   * @param accNo
   * @param extentedReport
   * @return true if the 'Select' button of the selected account is hidden and the selected account
   *         details are successfully verified; false otherwise.
   */
  public boolean verifyAccountSelection(String sortCode, String branch, String accName,
      String accNo, ExtentTest extentedReport) {
    boolean isOk = false;

    sortCode = sortCode.matches("\\d{2}-\\d{2}-\\d{2}") ? sortCode
        : sortCode.substring(0, 2) + sortCode.substring(2, 4) + sortCode.substring(4);
    String xpathAccSelect = xpathAccSelectBtnOfAccDetails.replaceAll("\\$\\{SORT_CODE\\}", sortCode)
        .replaceAll("\\$\\{BRANCH\\}", branch).replaceAll("\\$\\{ACC_NAME\\}", accName)
        .replaceAll("\\$\\{ACC_NO\\}", accNo);
    WebElement btnAccSelect = driver.findElement(By.xpath(xpathAccSelect));

    Log.message("Verifying the account selection", driver, extentedReport, true);
    if (btnAccSelect.isDisplayed()) {
      Log.message("Account selection failed. 'Select' button of the selected account is visible.",
          extentedReport);
    } else if (!verifyAccountDetails(sortCode, branch, accName, accNo, extentedReport)) {
      Log.message(
          "Account selection failed. Selected account details are not populated in 'Account Details' section.",
          extentedReport);
    } else {
      isOk = true;
    }

    return isOk;
  }

  /**
   * Selects the given day in 'Preferred Payment Day' dropdown.
   * 
   * @param day
   * @param extentReport
   * @param screenshot
   * @throws Exception
   */
  public void selectPreferredPaymentDay(String day, ExtentTest extentReport, boolean screenshot)
      throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, selectPreferredPaymentDay);
      Select preferredPayementDay = new Select(selectPreferredPaymentDay);
      preferredPayementDay.selectByVisibleText(day);
      WaitUtils.waitForSpinner(driver);
      Log.message("Perferred Payment day is selected as - " + day, driver, extentReport,
          screenshot);
    } catch (Exception e) {
      throw new Exception("Exception while selecting the preferred payment day : " + e);
    }
  }

  /**
   * Verifies that the actual error message in 'Account Details' section matches the expected error
   * message given.
   * 
   * @param expectedErrMsg
   * @param extentedReport
   * @return true if the actual error message matches the expected error message given; false
   *         otherwise.
   */
  public boolean verifyAccountDetailsErrorMessage(String expectedErrMsg,
      ExtentTest extentedReport) {
    String actualErrMsg = divAccDetErrMsg.getText().trim();
    Log.message("Actual Error Message: '" + actualErrMsg + "'", extentedReport);
    Log.message("Expected Error Message: '" + expectedErrMsg + "'", extentedReport);

    return actualErrMsg.equals(expectedErrMsg);
  }

  /**
   * Verifies that the actual error message in 'Account Details' section matches the expected error
   * message given, when no account details are added.
   * 
   * @param expectedErrMsg
   * @param extentedReport
   * @return true if the actual error message matches the expected error message given; false
   *         otherwise.
   */
  public boolean verifyNoAccountDetailsErrorMessage(String expectedErrMsg,
      ExtentTest extentedReport) {
    String actualErrMsg = divNoAccDetErrMsg.getText().trim();
    Log.message("Actual Error Message: '" + actualErrMsg + "'", extentedReport);
    Log.message("Expected Error Message: '" + expectedErrMsg + "'", extentedReport);

    return actualErrMsg.equals(expectedErrMsg);
  }

  /**
   * Verifies the 'Payment Schedule' table.
   * 
   * @param testData
   * @param extentedReport
   * @param screenshot
   * @return true if verification is successful; false otherwise.
   */
  public boolean verifyPaymentScheduleTable(HashMap<String, String> testData,
      ExtentTest extentedReport, boolean screenshot) {
    boolean isOk = false;
    String[] expectedColHeader = {"Due", "Calculated Premium", "Taxes", "Charges", "Charges Tax",
        "Instalments", "Amount Payable"};
    List<WebElement> lstColHeader = driver.findElements(By.cssSelector(cssPaySchedColHeader));
    int expectedColHeaderSize = expectedColHeader.length;
    int actualColHeaderSize = lstColHeader.size();
    boolean isHeaderOk = false;
    boolean isDataOk = false;
    boolean isFooterOk = false;

    GenericUtils.scrollIntoView(driver, divTotalAmountPayable);

    // Table header verification
    if (actualColHeaderSize != expectedColHeaderSize) {
      Log.message("The total number of actual colum headers is unexpected. Expected: <"
          + expectedColHeaderSize + ">; Actual: <" + actualColHeaderSize + ">", extentedReport);
    } else {
      for (int i = 0; i < expectedColHeaderSize; i++) {
        String actualColHeader = lstColHeader.get(i).getText().trim();
        isHeaderOk = actualColHeader.equals(expectedColHeader[i]);
        if (!isHeaderOk) {
          Log.message("The actual header '" + actualColHeader + "' of column #" + (i + 1)
              + " does not match the expected '" + expectedColHeader[i] + "'", extentedReport);
          break;
        }
      }
    }

    // Table data verification
    for (int i = 0; i < expectedColHeaderSize; i++) {
      String columnName = expectedColHeader[i];
      boolean isColumnOk = verifyPaymentScheduleColumn(columnName, testData, extentedReport, true);
      if (!isColumnOk) {
        Log.message("The column '" + columnName
            + "' of 'Payment Schedule' table does not have the expected data", extentedReport);
      }

      if (i > 0 && !isDataOk) {
        continue;
      } else {
        isDataOk = isColumnOk;
      }
    }

    /*
     * Table footer verification
     * 
     * All footers except at position 1 are covered during table data verification
     */
    String expectedFooter1 = "Total Premium";
    String actualFooter1 = divTotalPremium.getText().trim();
    isFooterOk = actualFooter1.equals(expectedFooter1);
    if (!isFooterOk) {
      Log.message("The actual footer '" + actualFooter1 + "' of column #1"
          + " does not match the expected '" + expectedFooter1 + "'", extentedReport);
    }

    isOk = isHeaderOk && isDataOk && isFooterOk;

    return isOk;
  }

  /**
   * Verifies the data in the given column on 'Payment Schedule' table.
   * 
   * @param columnName
   * @param testData
   * @param extentedReport
   * @param screenshot
   * @return true if the data in the given column are as expected; false otherwise.
   */
  public boolean verifyPaymentScheduleColumn(String columnName, HashMap<String, String> testData,
      ExtentTest extentedReport, boolean screenshot) {
    boolean isOk = false;
    int totalInstallments = 12;

    Log.message("Verifying the '" + columnName + "' column in 'Payment Schedule' ("
        + totalInstallments + " installments)", extentedReport);

    if (columnName.equals("Due")) { // #1
      int preferredPaymentDay = Integer.parseInt(testData.containsKey("Preferred Payment Day")
          ? testData.get("Preferred Payment Day") : DateTimeUtility.getCurrentDate().split("/")[0]);
      List<String> lstInstDates =
          DateTimeUtility.generateInstallmentDates(preferredPaymentDay, totalInstallments);
      List<WebElement> lstDueDates = driver.findElements(By.cssSelector(cssPaySchedDueDates));

      for (int i = 0; i < totalInstallments; i++) {
        String expectedDueDate = lstInstDates.get(i);
        String actualDueDate = lstDueDates.get(i).getText().trim();
        Log.message("Row No: [" + (i + 1) + "]; Expected Date: [" + expectedDueDate
            + "]; Actual Date: [" + actualDueDate + "]", extentedReport);
        isOk = actualDueDate.equals(expectedDueDate);
        if (!isOk) {
          break;
        }
      }
    } else if (columnName.equals("Calculated Premium")) { // #2
      String totalPayment = spnTotalPaymentAcceptanceTab.getText().trim();
      List<WebElement> lstCalculatedPremium =
          driver.findElements(By.cssSelector(cssPaySchedCalculatedPremium));
      float expectedTotalCalculatedPremium = 0;
      String actualTotalCalculatedPremium = divTotalCalculatedPremium.getText().trim();
      String firstPaymentAcceptanceTab = spnFirstPaymentAcceptanceTab.getText().trim();
      String installmentPaymentAcceptanceTab = spnInstallmentPaymentAcceptanceTab.getText().trim();
      boolean isOkFirstPayment = false;
      boolean isOkInstallmentPayment = false;
      boolean isOkTotalCalculatedPremium = false;

      for (int i = 0; i < totalInstallments; i++) {
        float actualCalculatedPremium =
            Float.parseFloat(lstCalculatedPremium.get(i).getText().trim());
        expectedTotalCalculatedPremium += actualCalculatedPremium;

        if (i == 0) {
          isOkFirstPayment =
              String.format("%.2f", actualCalculatedPremium).equals(firstPaymentAcceptanceTab);
          if (!isOkFirstPayment) {
            Log.message(
                "actualCalculatedPremium: [" + String.format("%.2f", actualCalculatedPremium)
                    + "]; firstPaymentAcceptanceTab: [" + firstPaymentAcceptanceTab + "]",
                extentedReport);
          }
        } else {
          if (i > 1 && !isOkInstallmentPayment) {
            continue;
          } else {
            isOkInstallmentPayment = String.format("%.2f", actualCalculatedPremium)
                .equals(installmentPaymentAcceptanceTab);
            if (!isOkInstallmentPayment) {
              Log.message("actualCalculatedPremium: ["
                  + String.format("%.2f", actualCalculatedPremium)
                  + "]; installmentPaymentAcceptanceTab: [" + installmentPaymentAcceptanceTab + "]",
                  extentedReport);
            }
          }
        }
      }

      isOkTotalCalculatedPremium =
          actualTotalCalculatedPremium.equals(String.format("%.2f", expectedTotalCalculatedPremium))
              && actualTotalCalculatedPremium.equals(totalPayment);
      isOk = isOkFirstPayment && isOkInstallmentPayment && isOkTotalCalculatedPremium;
      Log.message("isOkFirstPayment: [" + isOkFirstPayment + "]; isOkInstallmentPayment: ["
          + isOkInstallmentPayment + "]; isOkTotalCalculatedPremium: [" + isOkTotalCalculatedPremium
          + "]", extentedReport);
    } else if (columnName.equals("Taxes")) { // #3
      List<WebElement> lstTaxes = driver.findElements(By.cssSelector(cssPaySchedTaxes));
      float expectedTotalTaxes = 0;
      String actualTotalTaxes = divTotalTaxes.getText().trim();
      String expectedTaxes = "0.00";
      boolean isOkInstallmentTaxes = false;
      boolean isOkTotalTaxes = false;

      for (int i = 0; i < totalInstallments; i++) {
        float actualTaxes = Float.parseFloat(lstTaxes.get(i).getText().trim());
        expectedTotalTaxes += actualTaxes;

        if (i > 1 && !isOkInstallmentTaxes) {
          continue;
        } else {
          isOkInstallmentTaxes = String.format("%.2f", actualTaxes).equals(expectedTaxes);
        }
      }

      isOkTotalTaxes = actualTotalTaxes.equals(String.format("%.2f", expectedTotalTaxes));
      isOk = isOkInstallmentTaxes && isOkTotalTaxes;
      Log.message("isOkInstallmentTaxes: [" + isOkInstallmentTaxes + "]; isOkTotalTaxes: ["
          + isOkTotalTaxes + "]", extentedReport);
    } else if (columnName.equals("Charges")) { // #4
      List<WebElement> lstCharges = driver.findElements(By.cssSelector(cssPaySchedCharges));
      float expectedTotalCharges = 0;
      String actualTotalCharges = divTotalCharges.getText().trim();
      String expectedCharges = "0.00";
      boolean isOkInstallmentCharges = false;
      boolean isOkTotalCharges = false;

      for (int i = 0; i < totalInstallments; i++) {
        float actualCharges = Float.parseFloat(lstCharges.get(i).getText().trim());
        expectedTotalCharges += actualCharges;

        if (i > 1 && !isOkInstallmentCharges) {
          continue;
        } else {
          isOkInstallmentCharges = String.format("%.2f", actualCharges).equals(expectedCharges);
        }
      }

      isOkTotalCharges = actualTotalCharges.equals(String.format("%.2f", expectedTotalCharges));
      isOk = isOkInstallmentCharges && isOkTotalCharges;
      Log.message("isOkInstallmentCharges: [" + isOkInstallmentCharges + "]; isOkTotalCharges: ["
          + isOkTotalCharges + "]", extentedReport);
    } else if (columnName.equals("Charges Tax")) { // #5
      List<WebElement> lstChargesTax = driver.findElements(By.cssSelector(cssPaySchedChargesTax));
      float expectedTotalChargesTax = 0;
      String actualTotalChargesTax = divTotalChargesTax.getText().trim();
      String expectedChargesTax = "0.00";
      boolean isOkInstallmentChargesTax = false;
      boolean isOkTotalChargesTax = false;

      for (int i = 0; i < totalInstallments; i++) {
        float actualChargesTax = Float.parseFloat(lstChargesTax.get(i).getText().trim());
        expectedTotalChargesTax += actualChargesTax;

        if (i > 1 && !isOkInstallmentChargesTax) {
          continue;
        } else {
          isOkInstallmentChargesTax =
              String.format("%.2f", actualChargesTax).equals(expectedChargesTax);
        }
      }

      isOkTotalChargesTax =
          actualTotalChargesTax.equals(String.format("%.2f", expectedTotalChargesTax));
      isOk = isOkInstallmentChargesTax && isOkTotalChargesTax;
      Log.message("isOkInstallmentChargesTax: [" + isOkInstallmentChargesTax
          + "]; isOkTotalChargesTax: [" + isOkTotalChargesTax + "]", extentedReport);
    } else if (columnName.equals("Instalments")) { // #6
      List<WebElement> lstInstallments =
          driver.findElements(By.cssSelector(cssPaySchedInstallments));
      float expectedTotalInstallments = 0;
      String actualTotalInstallments = divTotalInstallments.getText().trim();
      String expectedInstallments = "0.00";
      boolean isOkInstallments = false;
      boolean isOkTotalInstallments = false;

      for (int i = 0; i < totalInstallments; i++) {
        float actualInstallments = Float.parseFloat(lstInstallments.get(i).getText().trim());
        expectedTotalInstallments += actualInstallments;

        if (i > 1 && !isOkInstallments) {
          continue;
        } else {
          isOkInstallments = String.format("%.2f", actualInstallments).equals(expectedInstallments);
        }
      }

      isOkTotalInstallments =
          actualTotalInstallments.equals(String.format("%.2f", expectedTotalInstallments));
      isOk = isOkInstallments && isOkTotalInstallments;
      Log.message("isOkInstallments: [" + isOkInstallments + "]; isOkTotalInstallments: ["
          + isOkTotalInstallments + "]", extentedReport);
    } else if (columnName.equals("Amount Payable")) { // #7
      String totalPayment = spnTotalPaymentAcceptanceTab.getText().trim();
      List<WebElement> lstAmountPayable =
          driver.findElements(By.cssSelector(cssPaySchedAmountPayable));
      float expectedTotalAmountPayable = 0;
      String actualTotalAmountPayable = divTotalAmountPayable.getText().trim();
      String firstPaymentAcceptanceTab = spnFirstPaymentAcceptanceTab.getText().trim();
      String installmentPaymentAcceptanceTab = spnInstallmentPaymentAcceptanceTab.getText().trim();
      boolean isOkFirstPayment = false;
      boolean isOkInstallmentPayment = false;
      boolean isOkTotalAmountPayable = false;

      for (int i = 0; i < totalInstallments; i++) {
        float actualAmountPayable = Float.parseFloat(lstAmountPayable.get(i).getText().trim());
        expectedTotalAmountPayable += actualAmountPayable;

        if (i == 0) {
          Log.message("Row No: [" + (i + 1) + "]; Expected Amount Payable: ["
              + firstPaymentAcceptanceTab + "]; Actual Amount Payable: ["
              + String.format("%.2f", actualAmountPayable) + "]; Sum of Amount Payable: ["
              + String.format("%.2f", expectedTotalAmountPayable) + "]", extentedReport);
          isOkFirstPayment =
              String.format("%.2f", actualAmountPayable).equals(firstPaymentAcceptanceTab);
        } else {
          Log.message("Row No: [" + (i + 1) + "]; Expected Amount Payable: ["
              + installmentPaymentAcceptanceTab + "]; Actual Amount Payable: ["
              + String.format("%.2f", actualAmountPayable) + "]; Sum of Amount Payable: ["
              + String.format("%.2f", expectedTotalAmountPayable) + "]", extentedReport);
          if (i > 1 && !isOkInstallmentPayment) {
            continue;
          } else {
            isOkInstallmentPayment =
                String.format("%.2f", actualAmountPayable).equals(installmentPaymentAcceptanceTab);
          }
        }
      }

      Log.message("Total Payment: [" + totalPayment + "]; Expected Total Amount Payable: ["
          + String.format("%.2f", expectedTotalAmountPayable) + "]; Actual Amount Payable: ["
          + actualTotalAmountPayable + "]", extentedReport);
      isOkTotalAmountPayable =
          actualTotalAmountPayable.equals(String.format("%.2f", expectedTotalAmountPayable))
              && actualTotalAmountPayable.equals(totalPayment);
      isOk = isOkFirstPayment && isOkInstallmentPayment && isOkTotalAmountPayable;
      Log.message("isOkFirstPayment: [" + isOkFirstPayment + "]; isOkInstallmentPayment: ["
          + isOkInstallmentPayment + "]; isOkTotalAmountPayable: [" + isOkTotalAmountPayable + "]",
          extentedReport);
    }

    return isOk;
  }

  /**
   * Returns the <i>testData</i> <code>HashMap</code> with payment details grabed from Cover tab.
   * 
   * @param testData
   * @param extentedReport
   * @param screenShot
   * @return the <code>HashMap</code> representation of testData, or null if no <code>HashMap</code>
   *         representation is applicable
   * @throws Exception
   */
  public HashMap<String, String> grabPaymentDetailsFromCoverTab(HashMap<String, String> testData,
      ExtentTest extentedReport, boolean screenShot) throws Exception {
    GenericUtils.scrollIntoView(driver, spnOneOffPaymentCoverTab);
    String oneOffPaymentCoverTab = spnOneOffPaymentCoverTab.getText().replaceFirst("£", "").trim();
    String totalPaymentCoverTab = spnTotalPaymentCoverTab.getText().replaceFirst("£", "").trim();
    String firstPaymentCoverTab = spnFirstPaymentCoverTab.getText().replaceFirst("£", "").trim();
    String installmentPaymentCoverTab =
        spnInstallmentPaymentCoverTab.getText().replaceFirst("£", "").trim();

    testData.put("oneOffPaymentCoverTab", oneOffPaymentCoverTab);
    testData.put("totalPaymentCoverTab", totalPaymentCoverTab);
    testData.put("firstPaymentCoverTab", firstPaymentCoverTab);
    testData.put("installmentPaymentCoverTab", installmentPaymentCoverTab);

    Log.message(
        "Cover Tab Payment Details:- One-Off Payment: [" + oneOffPaymentCoverTab
            + "]; Total Payment: [" + totalPaymentCoverTab + "]; First Payment: ["
            + firstPaymentCoverTab + "]; Installment Payment: [" + installmentPaymentCoverTab + "]",
        extentedReport);

    return testData;
  }

  /**
   * Verifies that the payment details available at Acceptance tab matches the payment details
   * grabed from Cover tab.
   * 
   * @param testData
   * @param extentedReport
   * @param screenShot
   * @return true if the payment details available at Acceptance tab are as expected; false
   *         otherwise.
   * @throws Exception
   */
  public boolean verifyPaymentDetailsAtAcceptanceTab(HashMap<String, String> testData,
      ExtentTest extentedReport, boolean screenShot) throws Exception {
    boolean isOk = false;

    try {
      GenericUtils.scrollIntoView(driver, spnOneOffPaymentAcceptanceTab);
      String oneOffPaymentCoverTab = testData.get("oneOffPaymentCoverTab");
      String totalPaymentCoverTab = testData.get("totalPaymentCoverTab");
      String firstPaymentCoverTab = testData.get("firstPaymentCoverTab");
      String installmentPaymentCoverTab = testData.get("installmentPaymentCoverTab");

      String oneOffPaymentAcceptanceTab = spnOneOffPaymentAcceptanceTab.getText().trim();
      String totalPaymentAcceptanceTab = spnTotalPaymentAcceptanceTab.getText().trim();
      String firstPaymentAcceptanceTab = spnFirstPaymentAcceptanceTab.getText().trim();
      String installmentPaymentAcceptanceTab = spnInstallmentPaymentAcceptanceTab.getText().trim();

      testData.put("oneOffPaymentAcceptanceTab", oneOffPaymentAcceptanceTab);
      testData.put("totalPaymentAcceptanceTab", totalPaymentAcceptanceTab);
      testData.put("firstPaymentAcceptanceTab", firstPaymentAcceptanceTab);
      testData.put("installmentPaymentAcceptanceTab", installmentPaymentAcceptanceTab);

      Log.message("Acceptance Tab Payment Details:- One-Off Payment: [" + oneOffPaymentAcceptanceTab
          + "]; Total Payment: [" + totalPaymentAcceptanceTab + "]; First Payment: ["
          + firstPaymentAcceptanceTab + "]; Installment Payment: ["
          + installmentPaymentAcceptanceTab + "]", extentedReport);

      boolean isOkOneOffPayment = oneOffPaymentAcceptanceTab.equals(oneOffPaymentCoverTab);
      boolean isOkTotalPayment = totalPaymentAcceptanceTab.equals(totalPaymentCoverTab);
      boolean isOkFirstPayment = firstPaymentAcceptanceTab.equals(firstPaymentCoverTab);
      boolean isOkInstallmentPayment =
          installmentPaymentAcceptanceTab.equals(installmentPaymentCoverTab);

      isOk = isOkOneOffPayment && isOkTotalPayment && isOkFirstPayment && isOkInstallmentPayment;
    } catch (Exception e) {
      throw new Exception("Exception in verifying payment details at Acceptance tab. " + e);
    }

    return isOk;
  }

  /**
   * Verifies that the actual includes fee matches the expected fee given.
   * 
   * @param expectedFee
   * @param extentedReport
   * @return true if the actual includes fee matches the expected fee given; false otherwise.
   */
  public boolean verifyIncludesFee(String expectedFee, ExtentTest extentedReport) {
    boolean isOk = false;
    String expectedIncludesFeeText = "Includes a fee of:£" + expectedFee;
    String actualIncludesFeeText1 = divIncludesFee1.getText().trim().replaceAll("\\n", "");
    String actualIncludesFeeText2 = divIncludesFee2.getText().trim().replaceAll("\\n", "");
    Log.message("Expected Fee Text: '" + expectedIncludesFeeText + "'", extentedReport);
    Log.message("Actual Fee Text #1: '" + actualIncludesFeeText1 + "'", extentedReport);
    Log.message("Actual Fee Text #2: '" + actualIncludesFeeText2 + "'", extentedReport);

    if (actualIncludesFeeText1.equals(expectedIncludesFeeText)
        && actualIncludesFeeText2.equals(expectedIncludesFeeText)) {
      isOk = true;
    } else {
      Log.message("The actual includes fee text #1 '" + actualIncludesFeeText1
          + "' or the actual includes fee text #2 '" + actualIncludesFeeText2
          + "' does not match the expected '" + expectedIncludesFeeText + "'", extentedReport);
    }

    return isOk;
  }

  /**
   * To select MidTermAdjustment Reason
   * 
   * @param extentedReport
   * @param MTAcreationReason
   * @throws Exception
   * 
   */



  /**
   * To Click Confirm button in Mid Term Adjustment
   * 
   * @throws Exception
   * @param extentedReport
   * 
   * 
   */
  /* jheelum */

  public CustDashboardPage confirmPaymentMTA(HashMap<String, String> testdata,
      ExtentTest extentedReport) throws Exception {
    CustDashboardPage objCustDashboardPage = null;
    try {// #C2__BUT_925B6D15BDB617A8717552
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Confirm button"))
              .until(ExpectedConditions.elementToBeClickable(btnConfirmMTAPaymet));
      btnConfirmMTAPaymet.click();
      Log.message("Clicked on Confirm button", driver, extentedReport, true);
      Thread.sleep(2000);
      WaitUtils.waitForSpinner(driver);
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Click on Confirm Button" + e);
    }
    return objCustDashboardPage;
  }



  public void selectMidTermAdjReason(String MTAcreationReason, ExtentTest extentedReport)
      throws Exception {
    try {

      Select titledrpDown = new Select(cmbMidTermAdjReason);
      titledrpDown.selectByVisibleText(MTAcreationReason);
      Log.event("MidTerm Reason Selected: " + MTAcreationReason);
      Thread.sleep(2000);
      Log.message("Selected the Reason : " + MTAcreationReason, " - " + extentedReport);
    } catch (Exception e) {
      throw new Exception("Date is not entered" + e);
    }
  }

  /**
   * To on Manage Policy
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void clickManagePolicy(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Manage Policy tab"))
              .until(ExpectedConditions.elementToBeClickable(drpManagePolicy));
      drpManagePolicy.click();
      Thread.sleep(3000);
      Log.message("Clicked on Manage Policy after upgrading policy", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Manage Policy Button :" + e);
    }
  }

  /**
   * To check Reverse Transaction is Disabled
   * 
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean isReverseTransactionLinkDisabled() throws Exception {
    boolean status = false;
    try {
      if (lnkReverseTransactionDisabled.getAttribute("class").contains("disable_attr"))
        status = true;
    } catch (Exception e) {
      throw new Exception("Unable to locate Reverse Transaction Link" + e);
    }
    return status;
  }

  /**
   * To Perform Renewals
   * 
   * @throws Exception
   * @param extentedReport
   * @param screenshot
   * 
   */

  public void performRenewals(ExtentTest extentedReport, boolean screenshot) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, drpManageRenewals);
      drpManageRenewals.click();
      WaitUtils.waitForElement(driver, drpoptInviteRenewals);
      Log.message("Clicked on Manage Renewals", driver, extentedReport, screenshot);
      drpoptInviteRenewals.click();
      Log.message("Clicked on Invite Renewals", driver, extentedReport, screenshot);
      WaitUtils.waitForElement(driver, btnWarningContinue);
      btnWarningContinue.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Warning Continue button", driver, extentedReport, screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click on Manage Policy Button :" + e);
    }
  }

  /**
   * To click payment option
   * 
   * @throws Exception
   * @param extentedReport
   * @param screenshot
   * 
   */

  public void clickPaymentOptionBar(ExtentTest extentedReport, boolean screenshot)
      throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      if (WaitUtils.waitForElement(driver, barPaymentOption)) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", barPaymentOption);
        Thread.sleep(2000);
      }
      Log.message("Clicked on Payment Options Bar", driver, extentedReport, screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click on Payment Options Bar :" + e);
    }
  }

  /**
   * To check PCL Account Details
   * 
   * @throws Exception : InterruptedException
   * @param extentedReport
   * 
   */

  public void selectPCLAccountDetails(ExtentTest extentedReport) throws InterruptedException {
    WaitUtils.waitForElement(driver, btnSelectAccountDetails);
    btnSelectAccountDetails.click();
    Thread.sleep(2000);
    Actions actions = new Actions(driver);
    actions.moveToElement(chkCustAgree).click().build().perform();
    Log.message("Selected the Payment option and Direct Debit", driver, extentedReport, true);
    Thread.sleep(1000);
    WaitUtils.waitForelementToBeClickable(driver, btnPaymentBuy, "Buy button is not enabled");
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("arguments[0].click();", btnPaymentBuy);
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked on Payment Buy", driver, extentedReport, true);
  }

  /**
   * To click Mid Term Adjustment
   * 
   * @throws Exception
   * @param extentedReport
   * 
   */

  public void clickMidTermAdjustment(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      lnkMidTermAdjust.click();
      WaitUtils.waitForElement(driver, txtMidTermDate);
      Log.message("Clicked on Mid Term Adjustment after upgrading policy", driver, extentedReport,
          true);
    } catch (Exception e) {
      throw new Exception("Unable to click on Mid Term Adjustment Button" + e);
    }
  }

  /**
   * Entering Date in Mid Term Adjustment
   * 
   * @throws Exception
   * @param extentedReport
   * 
   */

  public void enterDateForMTA(ExtentTest extentedReport) throws Exception {
    try {
      SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy");
      String strDate = formDate.format(new Date());
      txtMidTermDate.clear();
      txtMidTermDate.sendKeys(strDate);
      txtMidTermDate.click();
      Thread.sleep(2000);
      Log.message("Entered the Date : " + strDate, " - " + extentedReport);
    }

    catch (Exception e) {
      throw new Exception("Date is not entered", e);
    }
  }

  /**
   * To Click on Reverse Transaction Link
   * 
   * @throws Exception
   * @param extentedReport
   * 
   */

  public void clickReverseTransaction(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      lnkReverseTransaction.click();
      WaitUtils.waitForElement(driver, btnConfirmReversal);
      Log.message("Clicked on Reverse transaction after upgrading policy", driver, extentedReport,
          true);
    } catch (Exception e) {
      throw new Exception("Unable to click on Reverse transaction Button :" + e);
    }
  }

  /**
   * To Click continue option in Mid Term Adjustment
   * 
   * @throws Exception
   * @param extentedReport
   * 
   */

  public void clickMidTermContinue(ExtentTest extentedReport) throws Exception {
    try {
      btnMidTermContinue.click();
      WaitUtils.waitForSpinner(driver);
      Log.event("Mid Term Continue button clicked" + extentedReport);
    } catch (Exception e) {
      throw new Exception("Mid Term Continue button not clicked", e);
    }
  }

  /**
   * To click Buy option in Mid Term Adjustment
   * 
   * @throws Exception
   * @param extentedReport
   * @param screenshot
   * 
   */

  public void clickBuyMTA(ExtentTest extentedReport, boolean screenshot) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(btnBuyMTA));
      btnBuyMTA.click();
      Log.message("Clicked on Buy button", driver, extentedReport, screenshot);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Buy Button" + e);
    }
  }

  /**
   * To Click AddAll in Mid Term Adjustment
   * 
   * @throws Exception
   * @param extentedReport
   * @param screenshot
   * 
   */

  public void clickAddAllMTA(ExtentTest extentedReport, boolean screenshot) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Add all Details Link"))
              .until(ExpectedConditions.elementToBeClickable(btnAddAllMTA));
      btnAddAllMTA.click();
      Log.message("Clicked on Add all button", driver, extentedReport, screenshot);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on addall Button " + e);
    }
  }

  /**
   * To click Add All and Next button in Mid Term Adjustment
   * 
   * @throws Exception
   * @param extentedReport
   * @param screenshot
   * 
   */

  public void clickAddAllMTANextButton(ExtentTest extentedReport, boolean screenshot)
      throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnAddAllMTANext);
      btnAddAllMTANext.click();
      Log.message("Clicked on Add all  Next button", driver, extentedReport, screenshot);
      Thread.sleep(2000);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on add all Next Button " + e);
    }
  }

  /**
   * To Click Confirm button in Mid Term Adjustment
   * 
   * @throws Exception
   * @param extentedReport
   * 
   */

  public void clickConfirmMTA(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Confirm button"))
              .until(ExpectedConditions.elementToBeClickable(btnConfirmMTA));
      btnConfirmMTA.click();
      Log.message("Clicked on Confirm button", driver, extentedReport, true);
      Thread.sleep(2000);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Confirm Button" + e);
    }
  }

  /**
   * Returns Created Policy Number
   * 
   * @throws Exception
   * @param extentedReport
   * @return String
   * 
   */

  public String getCreatedPolicyNumber(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      return txtCreatedPolicy.getText();
    } catch (Exception e) {
      throw new Exception("Unable to get Policy Number", e);
    }
  }

  /**
   * Do eligibility check
   *
   * @param testdata
   * @param extentedReport
   * @param Screenshot :Boolean
   * @throws Exception
   * 
   */
  public void doEligibilityCheck(HashMap<String, String> testdata, boolean Screenshot,
      ExtentTest extentedReport) throws Exception {
    try {

      selectInsuredAddress("Insured Address");
      Log.message("Selected Insured Address ", extentedReport);
      selectUKResident("Yes");
      Log.message("Selected UK Resident as Yes", extentedReport);
      selectBankrupcy("No");
      Log.message("Selected Bankrupcy as No", extentedReport);
      clickCheckBox();
      Log.message("Entered details for eligibility check", driver, extentedReport, true);

    } catch (Exception e) {
      throw new Exception("Exception while doing eligibility check" + e);
    }
  }

  /**
   * To click confirm Reverse Transaction
   * 
   * @throws Exception
   * @param extentedReport
   * 
   */
  public void clickConfirmReverseTransaction(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Confirm button"))
              .until(ExpectedConditions.elementToBeClickable(btnConfirmReverseTransaction));
      btnConfirmReverseTransaction.click();
      Log.message("Clicked on Confirm button", driver, extentedReport, true);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Confirm Button" + e);
    }
  }

  /**
   * To verifies the MTA Status
   * 
   * @throws Exception
   * @param textToVerify
   * @return boolean
   * 
   */

  public boolean verifyMTAStatus(String textToVerify) throws Exception {
    if (!GenericUtils.verifyWebElementTextContains(textMTAStatus, textToVerify)) {
      return false;
    }
    return true;
  }

  /**
   * To click close button in Reverse transaction
   * 
   * @param extentReport
   * @param Screenshot :boolean
   * @throws Exception
   * 
   */
  public void clickCloseRevTransac(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, btnRevTransacClose, "No button not visible");
      btnRevTransacClose.click();
      Log.message("clicked on Close button", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click close button" + e);
    }
  }

  /**
   * select Insured address
   *
   * @param insuredaddress :String
   * @throws Exception
   * 
   */
  public void selectInsuredAddress(String insuredaddress) throws Exception {
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Unable to locate select insured address radio button"))
            .until(ExpectedConditions.visibilityOf(radinsurance_ele));
    try {

      selectRadioButton(radInsAddress, insuredaddress);
      Thread.sleep(1000);
      Log.event("Selected Insured Address :" + insuredaddress);
    } catch (Exception e) {
      throw new Exception("Unable to select Insure Address" + e);
    }
  }

  /**
   * select UK Resident
   *
   * @param ukresident :String
   * @throws Exception
   * 
   */
  public void selectUKResident(String ukresident) throws Exception {
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Unable to locate UK resident radio button"))
            .until(ExpectedConditions.visibilityOf(radUKResident_ele));

    try {
      selectRadioButton(radUKResident, ukresident);
      Thread.sleep(1000);
      Log.event("Selected UK Resident :" + ukresident);
    } catch (Exception e) {
      throw new Exception("Unable to select UK Resident" + e);
    }
  }

  /**
   * select Bankrupcy radioButton
   *
   * @param bankrupcy :String
   * @throws Exception
   * 
   */
  public void selectBankrupcy(String bankrupcy) throws Exception {
    try {

      selectRadioButton(radBankrupcy, bankrupcy);
      Thread.sleep(2000);
      Log.event("Selected Bankrupcy :" + bankrupcy);
    } catch (Exception e) {
      throw new Exception("Unable to select Bankrupcy" + e);
    }
  }

  /**
   * click checkbox
   *
   * @throw Exception : Custom Exception Message
   */
  public void clickCheckBox() throws Exception {
    try {

      selectcheckbox.click();
      Thread.sleep(2000);
      Log.event("Selected CheckBox");
    } catch (Exception e) {
      throw new Exception("Unable to select CheckBox" + e);
    }
  }

  /**
   * click continue button
   * 
   * @param extentedReport
   * @throw Exception : Custom Exception Message
   * 
   */
  public void clickContinue(ExtentTest extentedReport) throws Exception {
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Unable to find username text box"))
            .until(ExpectedConditions.visibilityOf(eligibilityCheck));
    try {

      eligibilityCheck.click();
      Thread.sleep(2000);
      Log.message("Clicked on Continue button sucessfully", extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Continue" + e);
    }
  }

  /**
   * click on validation & continue
   * 
   * @param extentedReport
   * @throw Exception : Custom Exception Message
   * 
   */
  public void clickValidationContinue(ExtentTest extentedReport) throws Exception {
    try {
      (new WebDriverWait(driver, 60).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find document read checkbox"))
              .until(ExpectedConditions.visibilityOf(validationCheck));
      validationCheck.click();
      Thread.sleep(2000);

      Log.message("Clicked on Continue", extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Continue" + e);
    }
  }

  /**
   * confirm Content
   *
   * @param extentedReport
   * @throw Exception : Custom Exception Message
   * 
   */
  public void confirmContent(ExtentTest extentedReport) throws Exception {
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Unable to find back bank validation button"))
            .until(ExpectedConditions.visibilityOf(backbankvalidation));
    try {
      Thread.sleep(2000);
      Log.event("Continue Checkbox found");
      Actions actions = new Actions(driver);
      actions.moveToElement(confirmcontent).click().build().perform();

      Log.message("Clicked on Continue button sucessfully", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Continue" + e);
    }
  }

  /**
   * Do validationCheck
   *
   * @param testdata : Hashmap
   * @param screenshot : Boolean
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void validationCheck(HashMap<String, String> testdata, boolean Screenshot,
      ExtentTest extentedReport) throws Exception {
    try {
      enterAccountHolder(testdata.get("First Name").toString());
      Log.message("Entered account holder name :" + testdata.get("First Name").toString(),
          extentedReport);
      enterSortCode(testdata.get("Sort Code").toString());
      Log.message("Entered sort code :" + testdata.get("Sort Code").toString(), extentedReport);
      enterAccountNumber(testdata.get("Account Number").toString());
      Log.message("Entered account number :" + testdata.get("Account Number").toString(),
          extentedReport);
      validateAccount();
      Log.message("Entered account has been validated", extentedReport);

    } catch (Exception e) {

      throw new Exception("Exception while doing eligibility check" + e);

    }
  }

  /**
   * Enter account holder details
   *
   * @param actholder : String
   * @throws Exception
   * 
   */

  public void enterAccountHolder(String actholder) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find username text box"))
              .until(ExpectedConditions.visibilityOf(txtActHolder));
      txtActHolder.clear();
      txtActHolder.sendKeys(actholder);
      Thread.sleep(2000);
      Log.event("Entered Account Holder Details");
    } catch (Exception e) {
      throw new Exception("Unable to Enter Account Holder Details" + e);
    }
  }

  /**
   * Enter sort code
   *
   * @param sortcode : String
   * @throws Exception
   * 
   */

  public void enterSortCode(String sortcode) throws Exception {
    try {
      txtSortCode.clear();
      txtSortCode.sendKeys(sortcode);
      Thread.sleep(2000);
      Log.event("Entered Sort Code");
    } catch (Exception e) {
      try {
        txtSortCode.clear();
        txtSortCode.sendKeys(sortcode);
        Thread.sleep(2000);
        Log.event("Entered Sort Code");
      } catch (Exception f) {
        throw new Exception("Unable to enter Sort Code" + f);
      }
    }
  }

  /**
   * Enter AccountNumber
   *
   * @param actnumber : String
   * @throws Exception
   * 
   */
  public void enterAccountNumber(String actnumber) throws Exception {
    try {
      txtActNumber.sendKeys(actnumber);
      Thread.sleep(2000);
      Log.event("Entered Account Number");
    } catch (Exception e) {
      try {
        txtActNumber.sendKeys(actnumber);
        Thread.sleep(2000);
        Log.event("Entered Account Number");
      } catch (Exception f) {
        throw new Exception("Unable to enter Account Number" + f);
      }

    }
  }

  /**
   * click validate button
   *
   * @throws Exception : Custom Exception Message
   * 
   */
  public void validateAccount() throws Exception {
    try {
      btnValidate.click();
      Thread.sleep(2000);
      Log.event("Validated Account Details");
    } catch (Exception e) {
      throw new Exception("Unable to select High Risk" + e);
    }
  }

  /**
   * Confirms the payment by clicking either 'Confirm' or 'Accept' button.
   * 
   * @param testdata
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   */

  public CustDashboardPage confirmPayment(HashMap<String, String> testdata,
      ExtentTest extentedReport) throws Exception {
    WaitUtils.waitForSpinner(driver);

    CustDashboardPage objCustDashboardPage = null;
    try {
      WaitUtils.waitForSpinner(driver);
      String paymentplan = testdata.get("Payment Plan");
      switch (paymentplan) {
        case "Annual":
          (new WebDriverWait(driver, 180).pollingEvery(2000, TimeUnit.MILLISECONDS)
              .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
              .withMessage("Unable to find Edit Details Link"))
                  .until(ExpectedConditions.elementToBeClickable(btnConfirmPayment));

          Thread.sleep(2000);
          btnConfirmPayment.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on 'Confirm' payment button", extentedReport);
          break;
        case "Monthly":
          clickAcceptbtn(extentedReport);
          break;
      }
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment. " + e);
    }
    return objCustDashboardPage;
  }

  /**
   * Confirms the payment by clicking either 'Confirm' or 'Accept' button.
   * 
   * @param testdata
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   */
  public CustDashboardPage rnlconfirmPayment(HashMap<String, String> testdata,
      ExtentTest extentedReport) throws Exception {
    CustDashboardPage objCustDashboardPage = null;
    try {
      String paymentplan = testdata.get("Payment Plan");
      switch (paymentplan) {
        case "Annual":
          (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
              .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
              .withMessage("Unable to find Edit Details Link"))
                  .until(ExpectedConditions.elementToBeClickable(rnlbtnAcceptPayment));
          rnlbtnAcceptPayment.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on 'Confirm' payment button", extentedReport);
          break;
        case "Monthly":
          (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
              .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
              .withMessage("Unable to find Edit Details Link"))
                  .until(ExpectedConditions.elementToBeClickable(btnAcceptPayment));
          btnAcceptPayment.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on 'Accept' payment button", extentedReport);
          break;
      }
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment. " + e);
    }
    return objCustDashboardPage;
  }

  /**
   * Cancels the payment by clicking 'Cancel'button.
   * 
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   */
  public CustDashboardPage cancelPayment(ExtentTest extentedReport) throws Exception {
    CustDashboardPage objCustDashboardPage = null;
    try {
      btnCancelPayment.click();
      Log.message("Clicked on 'Cancel' payment button", extentedReport);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, btnCancelPaymentYes,
          "Timed out to wait for 'Yes' button in cancel warning pop-up");
      btnCancelPaymentYes.click();
      Log.message("Clicked on 'Yes' button in cancel warning pop-up", extentedReport);
      WaitUtils.waitForSpinner(driver);
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to cancel the payment. " + e);
    }
    return objCustDashboardPage;
  }

  /**
   * Selects the payment plan.
   * 
   * @param paymentPlan
   * @param extentedReport
   * @throws Exception
   */
  public void selectPaymentPlan(String paymentPlan, ExtentTest extentedReport) throws Exception {
    try {
      (new WebDriverWait(driver, 300).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Timed out to find 'Payment Plan' dropdown"))
              .until(ExpectedConditions.elementToBeClickable(selectpaymentplan));

      Select paymentdropdown = new Select(selectpaymentplan);
      paymentdropdown.selectByVisibleText(paymentPlan);
      Log.message("Payment Plan Selected : " + paymentPlan, extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Plan" + e);
    }
  }

  /**
   * Selects the payment method.
   * 
   * @param paymentMethod
   * @param extentedReport
   * @throws Exception
   */
  public void selectPaymentMethod(String paymentMethod, ExtentTest extentedReport)
      throws Exception {
    try {
      Thread.sleep(2000);
      (new WebDriverWait(driver, 300).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Timed out to find 'Payment Method' dropdown"))
              .until(ExpectedConditions.elementToBeClickable(selectpaymentmethod));
      selectpaymentmethod.sendKeys(paymentMethod);
      Log.message("Payment Method Selected : " + paymentMethod, extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Method" + e);
    }
  }

  /**
   * select content insurance held field
   *
   * @param contins : String
   * @throws Exception
   * 
   */
  public void selectContIns(String contins) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to click contents-insurance held year dropdown"))
              .until(ExpectedConditions.elementToBeClickable(selectcontinsheld));
      Select cotentsdropdown = new Select(selectcontinsheld);
      cotentsdropdown.selectByValue(contins);
      Log.event("Contents insurance held years entered" + contins);
    } catch (Exception e) {
      throw new Exception("Unable to Select Contents insurance held year" + e);
    }
  }

  /**
   * select building insurance held field
   *
   * @param buildins : String
   * @throws Exception
   * 
   */
  public void selectbuildIns(String buildins) throws Exception {
    try {
      Select cotentsdropdown = new Select(selectbuildinsheld);
      cotentsdropdown.selectByValue(buildins);
      Log.event("Buildings insurance held years entered : " + buildins);
    } catch (Exception e) {
      throw new Exception("Unable to Select Buildings insurance held year" + e);
    }
  }

  /**
   * select cover
   *
   * @param cover : String
   * @throws Exception
   * 
   */
  public void selectCover(String cover) throws Exception {
    try {

      List<WebElement> buttonOptions = driver.findElements(By.cssSelector(radCover));

      for (int i = 0; i < buttonOptions.size(); i++) {
        radio_button_value = buttonOptions.get(i).getText();
        if (radio_button_value.equals(cover)) {
          buttonOptions.get(i).findElement(By.cssSelector("input")).click();
          radio_button_value = null;
          break;
        }
      }

      Log.event("Selected Cover :" + cover);
    } catch (Exception e) {
      throw new Exception("Unable to select Cover" + e);
    }
  }

  /**
   * select claims
   *
   * @param claim : String
   * @throws Exception
   * 
   */
  public void selectClaims(String claims) throws Exception {
    try {

      selectRadioButton(radClaims, claims);
      Log.event("Selected Cover :" + claims);
    } catch (Exception e) {
      throw new Exception("Unable to select Claims" + e);
    }
  }

  /**
   * Select Reverse Transaction Type
   *
   * @param revTransacType : String
   * @throws Exception
   * @param extentedReport
   * 
   */

  public void selectRevTransacType(String revTransacType, ExtentTest extentedReport)
      throws Exception {
    try {

      Select typedrpDown = new Select(cmbTypeReverTransac);
      typedrpDown.selectByVisibleText(revTransacType);
      Log.event("Rev transaction type Selected: " + revTransacType);
      Thread.sleep(2000);
      Log.message("Selected the type : " + revTransacType, " - " + extentedReport);
    } catch (Exception e) {
      throw new Exception("Type is not selected" + e);
    }
  }

  /**
   * select personal
   *
   * @param personal : String
   *
   * @throws Exception
   * 
   */
  public void selectPersonal(String personal) throws Exception {
    try {
      selectRadioButton(radPersonal, personal);
      Log.event("Selected Personal :" + personal);
    } catch (Exception e) {
      throw new Exception("Unable to select Personal" + e);
    }
  }

  /**
   * select Bicycle
   *
   * @param bicycle : String
   * @throws Exception
   * 
   */
  public void selectBicyle(String bicycle) throws Exception {
    try {
      selectRadioButton(radBicycle, bicycle);
      Log.event("Selected Bicycle :" + bicycle);
    } catch (Exception e) {
      throw new Exception("Unable to select Bicycle" + e);
    }
  }

  /**
   * select HighRiskItems
   *
   * @param highrisk : String
   * @throws Exception
   * 
   */
  public void selectHighrisk(String highrisk) throws Exception {
    try {
      WaitUtils.waitForElement(driver, driver.findElement(By.cssSelector(radHighrisk)), 3);
      selectRadioButton(radHighrisk, highrisk);
      Log.event("Selected High Risk :" + highrisk);
    } catch (Exception e) {
      try {
        selectRadioButton(radHighrisk, highrisk);
        Log.event("Selected High Risk :" + highrisk);
      } catch (Exception f) {
        throw new Exception("Unable to select High Risk" + f);
      }

      throw new Exception("Unable to select High Risk" + e);
    }
  }

  /**
   * select Agree
   *
   * @param agree : String
   * @param extentReport
   * @throws Exception
   * 
   */
  public void selectAgree(String agree, ExtentTest extentReport) throws Exception {
    try {

      WaitUtils.waitForElement(driver, radAgree_Ele);
      selectRadioButton(radAgree, agree);
      Log.message("Selected Agreement Statement : " + agree, extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to select Agreement Statement" + e);
    }
  }

  /**
   * select NumberofBedrooms
   *
   * @param noofbedrooms : String
   * @throws Exception
   * 
   */
  public void selectNumberofBedrooms(String noofbedrooms) throws Exception {
    try {
      WaitUtils.waitForElement(driver, selectnoofbedroom);
      Select bedroom = new Select(selectnoofbedroom);
      bedroom.selectByValue(noofbedrooms);
      Log.event("Number of Bedrooms Selected : " + noofbedrooms);
    } catch (Exception e) {
      throw new Exception("Unable to Select Number of Bedrooms" + e);
    }
  }

  /**
   * select Property type
   *
   * @param propertyType : String
   * @throws Exception
   * 
   */
  public void selectPropertyType(String propertyType) throws Exception {
    try {
      Select propery = new Select(selectpropertytype);
      propery.selectByVisibleText(propertyType);
      Thread.sleep(2000);
      propery.selectByVisibleText(propertyType);
      Log.event("Property Selected : " + propertyType);
    } catch (Exception e) {
      throw new Exception("Unable to Select Property Type" + e);
    }
  }

  /**
   * click Add Home Emergency
   *
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */
  public void clickAddHomeEmergency(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(btnAddHomeEmergency));
      btnAddHomeEmergency.click();

      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on home emergency add button", driver, extentedReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Add Button" + e);
    }

  }

  /**
   * click upgradenow (upgrading policy from 3* to 5*)
   *
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   * 
   */

  public void clickUpgradeNow(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, standardOrExtraDropdown);
      Select policyTypeDropdown = new Select(standardOrExtraDropdown);
      policyTypeDropdown.selectByVisibleText("Extra");
      WaitUtils.waitForElement(driver, switchToButton);
      switchToButton.click();
      Log.message("Clicked on Upgrade Now button", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);
      /*
       * WaitUtils.waitForSpinner(driver); WaitUtils.waitForElement(driver, btnUpgradeNow);
       * btnUpgradeNow.click(); Log.message("Clicked on Upgrade Now button", driver,
       * extentedReport); WaitUtils.waitForSpinner(driver);
       */
    } catch (Exception e) {
      throw new Exception("Unable to click on Upgrade Now Button :" + e);
    }
  }

  /**
   * verify policy upgrade
   * 
   * 
   */
  public boolean verifyUpgradePolicyTo5Star() {
    boolean status = false;
    // if (GenericUtils.verifyWebElementTextContains(button3StarProduct, "3 Star Product"))
    Select select = new Select(standardOrExtraDropdown);
    WebElement selectedOption = select.getFirstSelectedOption();
    if (selectedOption.getText().equalsIgnoreCase("Extra"))
      status = true;
    System.out.println("current policy scheme is "  + selectedOption.getText());
    return status;
  }

  /**
   * click recalculate (recalculate premium)
   *
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   * 
   */
  public void clickReCalculate(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForElement(driver, btnReCalculate);
      btnReCalculate.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Re-Calculate after upgrading policy", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Re-Calculate Button :" + e);
    }
  }

  /**
   * validate Documents (pcl selected as payment method)
   *
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */

  public void ValidateDocuments(ExtentTest extentedReport) throws Exception {
    try {

      boolean docPresence;
      boolean docPresence1 = true;

      Thread.sleep(20000);
      List<String> docList =
          Arrays.asList("Pre-Contractual Agreement:", "Representative Example:", "DD Mandate:");
      List<WebElement> docOptions = driver.findElements(By.cssSelector(impDocuments));
      for (int j = 0; j < docList.size(); j++) {
        docPresence = false;
        for (int i = 0; i < docOptions.size();) {

          String doc_Name = docOptions.get(i).getText();
          if (doc_Name.equals(docList.get(j)))
            ;

          System.out.println(
              "Values are printed here in  actual:  Expected" + doc_Name + ":" + docList.get(j));
          docPresence = true;
          break;
        }

        docPresence1 = docPresence && docPresence1;
      }

      if (docPresence1) {
        Log.message("Verified the three monthly policy documents visible ", driver, extentedReport,
            true);
      } else {
        Log.fail("Step-12:Three monthly policy documents are not visible", driver, extentedReport,
            true);
      }

    } catch (Exception e) {
      throw new Exception("Unable to get the documents when PCL mode selected " + e);
    }

  }

  /**
   * Enter customer details for buildings
   * 
   * @throws Exception
   * @param testdata : Hashmap
   * @param Screenshot :Screenshot
   */
  public void enterCustomerDetailsForBuildings(HashMap<String, String> testdata, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      selectClaims(testdata.get("Claims").toString());
    } catch (Exception e) {
      throw new Exception("Unable to Enter Customer Details for Building cover" + e);
    }
  }

  /**
   * Get Default values of Insurance covers
   * 
   * @return String
   * @param extentedReport
   * @throws Exception
   * @param testDataToCheck : string
   * @param Screenshot :boolean
   */
  public String getDefaultVals_InsCovers(String testDataToCheck, boolean screenshot,
      ExtentTest extentedReport) throws Exception {

    String[] coversToAdd = testDataToCheck.split("_");
    String coverSec = coversToAdd[0];
    String Exp_limit_Amt = coversToAdd[1];
    String Exp_Acess_Amt = coversToAdd[2];

    WaitUtils.waitForSpinner(driver);
    String valToReturn = "false";
    try {
      switch (coverSec) {

        case "BLD":
          String eletoInteract_lmt = tbl_insurance_covers_Bld + ":nth-child(1)>td:nth-child(3)";
          String eletoInteract_exc = tbl_insurance_covers_Bld + ":nth-child(1)>td:nth-child(4)";
          String Lmt_Val = driver.findElement(By.cssSelector(eletoInteract_lmt)).getText();
          String exces_Val = driver.findElement(By.cssSelector(eletoInteract_exc)).getText();
          System.out.println("Limit and Excess amount of BLD cover is: LimitVal " + Lmt_Val
              + ",ExcessValue:" + exces_Val);
          if ((Lmt_Val.equals(Exp_limit_Amt)) && (exces_Val.equals(Exp_Acess_Amt))) {
            valToReturn = "BLD Cover : LimitAmt =" + Lmt_Val + ", ExcessAmt =" + exces_Val;
          } else
            Log.message("Default amount were not populated for BLD Cover, it displayed : LimitAmt ="
                + Lmt_Val + ", ExcessAmt =" + exces_Val, driver, extentedReport, true);
          break;

        case "CNT":
          System.out.println("Contents!");
          eletoInteract_lmt = tbl_insurance_covers_CNT + ":nth-child(1)>td:nth-child(3)";
          eletoInteract_exc = tbl_insurance_covers_CNT + ":nth-child(1)>td:nth-child(4)";
          Lmt_Val = driver.findElement(By.cssSelector(eletoInteract_lmt)).getText();
          exces_Val = driver.findElement(By.cssSelector(eletoInteract_exc)).getText();
          System.out.println("Limit and Excess amount of CNT cover is: LimitVal " + Lmt_Val
              + ",ExcessValue:" + exces_Val);
          if ((Lmt_Val.equals(Exp_limit_Amt)) && (exces_Val.equals(Exp_Acess_Amt))) {
            valToReturn = "CNT Cover : LimitAmt =" + Lmt_Val + ", ExcessAmt =" + exces_Val;
          } else
            // Log.message("Default amount were not populated for
            // contents cover", driver,
            // extentedReport, true);
            Log.message("Default amount were not populated for CNT Cover, it displayed : LimitAmt ="
                + Lmt_Val + ", ExcessAmt =" + exces_Val, driver, extentedReport, true);
          break;

        case "PI":
          System.out.println("Personal Items!");
          eletoInteract_lmt = tbl_insurance_covers_CNT + ":nth-child(6)>td:nth-child(3)";
          eletoInteract_exc = tbl_insurance_covers_CNT + ":nth-child(6)>td:nth-child(4)";
          Lmt_Val = driver.findElement(By.cssSelector(eletoInteract_lmt)).getText();
          exces_Val = driver.findElement(By.cssSelector(eletoInteract_exc)).getText();
          System.out.println("Limit and Excess amount of CNT cover is: LimitVal " + Lmt_Val
              + ",ExcessValue:" + exces_Val);
          if ((Lmt_Val.equals(Exp_limit_Amt)) && (exces_Val.equals(Exp_Acess_Amt))) {
            valToReturn =
                "PersonalItems Cover : LimitAmt =" + Lmt_Val + ", ExecessAmt =" + exces_Val;
          } else
            Log.message("Default amount were not populated for Personal items cover", driver,
                extentedReport, true);
          break;
      }

      return valToReturn;

    }

    catch (Exception e) {
      throw new Exception(
          "Unable to get the default values from BLD& CNT section. Exception occured: " + e);
    }

  }

  /**
   * select Insurance item
   *
   * @param coverDetails : string
   * @param Screenshot :boolean
   * @param extentedReport
   * @return String
   * @throws Exception
   * 
   */
  public String SelectInsuranceItem(String coverDetails, boolean screenshot,
      ExtentTest extentedReport) throws Exception {

    String RowToValidate = null;
    String insurance_covers = "";
    String tagToFind = "a";
    String[] coversToAdd = coverDetails.split("_");
    String inputcoverToSelect = coversToAdd[0];
    String coverSection = coversToAdd[1];
    String addRemove = coversToAdd[2];
    if (coverSection.equalsIgnoreCase("CNT")) {
      insurance_covers = tbl_insurance_covers_CNT;

    } else if (coverSection.equalsIgnoreCase("BLD")) {
      insurance_covers = tbl_insurance_covers_Bld;
    } else {
      insurance_covers = tbl_insurnace_covers_AddOns;
      if (addRemove.equalsIgnoreCase("Add")) {
        tagToFind = "button";
      }
    }

    try {
      List<WebElement> inscoverRows = driver.findElements(By.cssSelector(insurance_covers));

      for (int i = 0; i < inscoverRows.size(); i++) {
        List<WebElement> cover_Name = inscoverRows.get(i).findElements(By.tagName("td"));

        for (int j = 0; j < cover_Name.size(); j++) {

          if ((!(cover_Name.get(j).getText().toString() == null)) && (inputcoverToSelect.trim()
              .equalsIgnoreCase(cover_Name.get(j).getText().toString().trim()))) {
            RowToValidate = insurance_covers;
            List<WebElement> buttonToClick =
                inscoverRows.get(i).findElements(By.tagName(tagToFind));
            for (int k = 0; k < buttonToClick.size(); k++) {
              if (buttonToClick.get(k).isDisplayed()
                  && buttonToClick.get(k).getText().equalsIgnoreCase(addRemove)) {
                buttonToClick.get(k).sendKeys(Keys.ENTER);
                return RowToValidate;
              }
            }
          }

        }
      }

      if (RowToValidate != null) {
        Log.message("Clicked on " + inputcoverToSelect + "covers button successfully, to "
            + addRemove + " " + coverSection + " cover", driver, extentedReport, true);
      }

    }

    catch (Exception e) {
      throw new Exception(
          "Unable to Click on " + addRemove + " Button to add covers " + e.getMessage());
    }

    return RowToValidate;
  }

  /**
   * Enter insurance cover details
   *
   * @throws Exception
   * @return boolean
   * @param testdata : hashmap
   * @param InscoverToAdd :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enterInsCoverDetails(HashMap<String, String> testData, String InscoverToAdd,
      String RowToInteract, boolean screenshot, ExtentTest extentedReport) throws Exception {

    boolean BoolVal = false;
    String coverType = InscoverToAdd.split("_")[0];
    String addRemovebuton = InscoverToAdd.split("_")[2];

    try {

      switch (coverType) {
        case "Buildings":
          BoolVal = enter_BLDCoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          System.out.println("Buildings!");
          break;

        case "Accidental Damage":
          System.out.println("Accidental Damage!");
          BoolVal = enter_ADCoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          break;

        case "High Risk Items":
          System.out.println("High Risk Items!");
          BoolVal = enter_HRCoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          break;

        case "Garden cover":
          System.out.println("Garden cover!");
          BoolVal = enter_GCCoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          break;

        case "Technology & Entertainment":
          System.out.println("Technology & Entertainment!");
          BoolVal = enter_TECoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          break;

        case "Personal Items":
          System.out.println("Personal Items!");
          BoolVal = enter_PIcoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          break;

        case "Pedal Cycles":
          System.out.println("Pedal Cycles!");
          BoolVal = enter_PCcoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          break;

        case "Home Emergency":
          System.out.println("Home Emergency");
          BoolVal = enter_HEcoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          break;

        case "Legal Expenses":
          System.out.println("Legal Expenses!");
          BoolVal = enter_LEcoverDetails(testData, coverType, addRemovebuton, RowToInteract, true);
          break;
      }

      return BoolVal;
    }

    catch (Exception e) {
      throw new Exception("Unable to " + addRemovebuton + " " + coverType + "_"
          + InscoverToAdd.split("_")[1] + "cover details. Exception occured: " + e);
    }

  }

  /**
   * Enter Building coverDetails
   * 
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_BLDCoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      String eletoInteract = RowToInteract + ":nth-child(1)>td:nth-child(3)";
      if (AddRem.equalsIgnoreCase("Add")) {

        WaitUtils.waitForElementPresent(driver, ins_BLDCover_EditPopUp,
            "Unable to find the Buildings cover Edit window");
        String strPopName =
            ins_BLDCover_EditPopUp.findElement(By.className("modal-title")).getText();
        if (strPopName.equalsIgnoreCase(coverType)) {
          WebElement ele_BldExces =
              ins_BLDCover_EditPopUp.findElement(By.cssSelector(ins_BldCover_excessAmt));
          if (ele_BldExces.isDisplayed()) {
            ele_BldExces.sendKeys(testData.get("BLD_ExcessAmt").toString());
            ins_BldCover_EditSaveBut.click();
          }
        }
        WaitUtils.waitForinvisiblityofElement(driver, 180, popup_edit_ins_Bld,
            "Edit pop failed to close after waiting for 3 mins");
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after adding Building cover");

        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val.contains(testData.get("BLD_ExcessAmt").toString())) {
          BoolVal = true;
        }
      } else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after adding Pedal cycle contents");
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val == "") {
          BoolVal = true;
        }
      }

      return BoolVal;
    }

    catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }
  }

  /**
   * Enter Accidental Damage CoverDetails
   *
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_ADCoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      String eletoInteract = RowToInteract + ":nth-child(2)>td:nth-child(4)";
      WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
          "Unable to Switch to Windows after " + AddRem + "ing Accidental damage cover");
      if (AddRem.equalsIgnoreCase("Add")) {
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val.contains(testData.get("AD_ExcessAmt").toString())) {
          BoolVal = true;
        }
      } else {
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val == "") {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }
  }

  /**
   * Enter High Risk Items CoverDetails
   *
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_HRCoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      String eletoInteract = RowToInteract + ":nth-child(3)>td:nth-child(3)";
      if (AddRem.equalsIgnoreCase("Add")) {
        WaitUtils.waitForElementPresent(driver, ins_HRcoverPopUp,
            "Unable to find the High Risk covers popup");
        String strPopName = ins_HRcoverPopUp.findElement(By.className("modal-title")).getText();
        if (strPopName.equalsIgnoreCase(coverType)) {
          selectRadioButton(rad_ins_HRcover, "Yes");
          WaitUtils.waitForElementPresent(driver, ins_HRcoverRisk1,
              "Unable to find the HR covers when Yes button clicked");
          ins_HRcoverRisk1.sendKeys(testData.get("HR_NoOfItems").toString());
          WaitUtils.waitForElementPresent(driver, ins_HR_Category,
              "Unable to find the High Risk category field");
          ins_HR_Category.sendKeys(testData.get("HR_Category").toString());
          Thread.sleep(2000);
          WaitUtils.waitForElementPresent(driver, ins_HR_Desc,
              "Unable to find the High Risk description field");
          ins_HR_Desc.click();
          Thread.sleep(1000);
          ins_HR_Desc.sendKeys(testData.get("HR_ItemDescription").toString());
          ins_HR_itemVal.click();
          ins_HR_itemVal.sendKeys(testData.get("HR_ItemVaue").toString());

          // WaitUtils.waitForElementPresent(driver, ins_HR_itemVal,
          // "Unable to find the High Risk Item Value field");dd

          Actions actions = new Actions(driver);
          /*
           * actions.moveToElement(ins_HR_Desc).click();
           * actions.sendKeys(testData.get("HR_ItemDescription").toString());
           * actions.sendKeys(Keys.RETURN).build().perform(); ins_HR_Desc.click();
           * Thread.sleep(1000); ins_HR_Desc.sendKeys(Keys.TAB);
           */

          /*
           * actions = new Actions(driver); actions.moveToElement(ins_HR_itemVal).click();
           * actions.sendKeys(testData.get("HR_ItemVaue").toString()); Thread.sleep(2000);
           * actions.sendKeys(Keys.RETURN).build().perform();
           */

          Thread.sleep(2000);
          WaitUtils.waitForElementPresent(driver, ins_HR_SaveBut,
              "Unable to Save button on HR pop up");
          ins_HR_SaveBut.click();
          WaitUtils.waitForinvisiblityofElement(driver, 180, PopupWnd_ins_HRCover,
              "High Risk Items window failed to close after waiting for 3 mins");
          String Limit_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
          System.out.println("Value is " + Limit_Val);
          if (Limit_Val.contains(testData.get("HR_ItemVaue").toString())) {
            BoolVal = true;
          }
        }

      } else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after " + AddRem + "ing high risk cover");
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val == "") {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }
  }

  /**
   * Enter Garden CoverDetails
   *
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_GCCoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      String eletoInteract = RowToInteract + ":nth-child(4)>td:nth-child(3)";
      if (AddRem.equalsIgnoreCase("Add")) {
        WaitUtils.waitForElementPresent(driver, ins_GCcover_PopUp,
            "Unable to find the Gardencovers popup");
        String strPopName = ins_GCcover_PopUp.findElement(By.className("modal-title")).getText();
        if (strPopName.equalsIgnoreCase("Garden Contents")) {
          ins_GCcover_Limit.sendKeys(testData.get("GC_ItemVaue").toString());
          WaitUtils.waitForElementPresent(driver, ins_GCcover_saveBut,
              "Unable to find the save button on Gardencovers popup");
          ins_GCcover_saveBut.click();
          WaitUtils.waitForinvisiblityofElement(driver, 180, PopupWnd_ins_GCcover,
              "Garden Contents window failed to close after waiting for 3 mins");
          WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
              "Unable to Switch to Windows after " + AddRem + "ing garden cover");
          String Limit_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
          System.out.println("Value is " + Limit_Val);
          if (Limit_Val.contains(testData.get("GC_ItemVaue").toString())) {
            BoolVal = true;
          }

        }
      } else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after " + AddRem + "ing garden cover");
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val == "") {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }
  }

  /**
   * Enter Technology and Entertainment CoverDetails
   * 
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_TECoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      String eletoInteract = RowToInteract + ":nth-child(5)>td:nth-child(3)";
      if (AddRem.equalsIgnoreCase("Add")) {
        WaitUtils.waitForElementPresent(driver, ins_TEcover_Popup,
            "Unable to find the Gardencovers popup");
        String strPopName = ins_TEcover_Popup.findElement(By.className("modal-title")).getText();
        if (strPopName.equalsIgnoreCase(coverType)) {
          ins_TEcover_Limit.sendKeys(testData.get("TE_coverLimit").toString());
          WaitUtils.waitForElementPresent(driver, ins_TEcover_saveBut,
              "Unable to find the save button on Technology & Entertainment popup");
          ins_TEcover_saveBut.click();
          Thread.sleep(2000);
          WaitUtils.waitForinvisiblityofElement(driver, 180, PopupWnd_ins_TEcover,
              coverType + " window failed to close after waiting for 3 mins");
          String Limit_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
          // Limit_Val = Limit_Val.trim();
          Limit_Val = Limit_Val.substring(2);
          System.out.println("Value is " + Limit_Val);
          if (Limit_Val.contains(testData.get("TE_coverLimit").toString())) {
            BoolVal = true;
          }
        }

      } else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after " + AddRem
                + "ing Technology and Entertainment cover");
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val == "") {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }
  }

  /**
   * Enter Personal Items CoverDetails
   *
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_PIcoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      String eletoInteract = RowToInteract + ":nth-child(6)>td:nth-child(3)";
      if (AddRem.equalsIgnoreCase("Add")) {
        WaitUtils.waitForElementPresent(driver, ins_PIcover_Popup,
            "Unable to find the Personal Items popup");
        String strPopName = ins_PIcover_Popup.findElement(By.className("modal-title")).getText();
        if (strPopName.trim().equalsIgnoreCase(coverType)) {
          WaitUtils.waitForElementPresent(driver, ins_PI_coverReq,
              "Unable to find the cover requied field on " + coverType + "popup");
          ins_PI_coverReq.sendKeys(testData.get("PI_Requiredcover").toString());
          selectRadioButton(rad_ins_PIcover, "Yes");
          WaitUtils.waitForElementPresent(driver, ins_PI_NoOfItem,
              "Unable to find the no of item cover field on " + coverType + "popup");
          ins_PI_NoOfItem.sendKeys(testData.get("PI_noOfItems").toString());
          WaitUtils.waitForElementPresent(driver, ins_PI_Category,
              "Unable to find the category field on " + coverType + "popup");
          ins_PI_Category.sendKeys(testData.get("PI_category").toString());

          WaitUtils.waitForElementPresent(driver, ins_PI_Desc,
              "Unable to find the description field on " + coverType + "popup");
          ins_PI_Desc.sendKeys(testData.get("PI_Description").toString());
          WaitUtils.waitForElementPresent(driver, ins_PI_ItemValue,
              "Unable to find the Item value field on " + coverType + "popup");
          ins_PI_ItemValue.sendKeys(testData.get("PI_ItemValue").toString());
          WaitUtils.waitForElementPresent(driver, ins_PI_SaveBtn,
              "Unable to find the save button on " + coverType + "popup");
          ins_PI_SaveBtn.click();

          WaitUtils.waitForinvisiblityofElement(driver, 360, PoupWnd_ins_PI,
              coverType + " window failed to close after waiting for 3 mins");

          String Limit_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
          System.out.println("Value is " + Limit_Val);
          int valuetochk = Integer.parseInt(testData.get("PI_Requiredcover").toString())
              + Integer.parseInt(testData.get("PI_ItemValue").toString());

          if (Limit_Val.contains(Integer.toString(valuetochk))) {
            BoolVal = true;
          }
        }
      } else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after " + AddRem + "ing personal items cover");
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val == "") {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }
  }

  /**
   * Enter pedal cycles CoverDetails
   *
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_PCcoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      WaitUtils.waitForSpinner(driver);
      String eletoInteract = RowToInteract + ":nth-child(7)>td:nth-child(3)";
      if (AddRem.equalsIgnoreCase("Add")) {
        WaitUtils.waitForElementPresent(driver, ins_PCcover_Popup,
            "Unable to find the Pedal cycles popup");
        String strPopName = ins_PCcover_Popup.findElement(By.className("modal-title")).getText();
        if (strPopName.equalsIgnoreCase("Unspecified Bicycles")) {
          ins_PCcover_UnSp_cycleCount.sendKeys(testData.get("PC_UnspecifiedItems").toString());
          WaitUtils.waitForElementPresent(driver, ins_PCcover_coverAmt,
              "Unable to find the unspecifiedcover amt on Pedalcylcles popup");
          ins_PCcover_coverAmt.sendKeys(testData.get("PC_UnspecifiedItems_coverAmt").toString());
          ins_PCcover_coverAmt.click();
          WaitUtils.waitForElementPresent(driver, ins_PC_cover_RadioBut,
              "Unable to find the specified Pedalcyle radio button");
          selectRadioButton(rad_ins_PC, "Yes");
          Thread.sleep(2000);
          WaitUtils.waitForElementPresent(driver, ins_PCcover_Specified_cyclecount,
              "Unable to find the specified Item nodes on Pedalcycle window");
          if (ins_PCcover_Specified_cyclecount.isDisplayed()) {
            Select pcSpecItem = new Select(ins_PCcover_Specified_cyclecount);
            pcSpecItem.selectByValue((testData.get("PC_specifiedItems")));
            WaitUtils.waitForElementPresent(driver, ins_PCcover_Description,
                "Unable to find the specified Item description field on Pedalcycle window");
            Thread.sleep(2000);
            /*
             * Actions actions = new Actions(driver);
             * actions.moveToElement(ins_PCcover_Description).click();
             * actions.sendKeys(testData.get("PC_specifiedItems_Desc").toString());
             */
            ins_PCcover_Description.click();
            ins_PCcover_Description.sendKeys(testData.get("PC_specifiedItems_Desc").toString());

            Thread.sleep(2000);
            // actions.sendKeys(Keys.RETURN).build().perform();
            WaitUtils.waitForElementPresent(driver, ins_PCcover_value,
                "Unable to find the specified Item description field on Pedalcycle window");
            // ins_PCcover_value.sendKeys(testData.get("PC_specifiedItems_coverAmt").toString());
            /*
             * actions = new Actions(driver); actions.moveToElement(ins_PCcover_value).click();
             * actions.sendKeys(testData.get( "PC_specifiedItems_coverAmt").toString());
             * Thread.sleep(2000); actions.sendKeys(Keys.RETURN).build().perform();
             */
            /*
             * ins_PCcover_Description.click(); ins_PCcover_Description.sendKeys(Keys.TAB);
             */

            ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('C2__QUE_B9E1D767142BE41B1646797_R1').value= '"
                    + testData.get("PC_specifiedItems_coverAmt") + "';");

            WaitUtils.waitForElementPresent(driver, ins_PCcover_SaveBut,
                "Unable to find the save button on Pedalcycle window");
            ins_PCcover_SaveBut.click();
          }

          WaitUtils.waitForinvisiblityofElement(driver, 300, PopupWnd_ins_PC,
              coverType + " window failed to close after waiting for 3 mins");
          WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
              "Unable to Switch to Windows after adding Pedal cycle contents");
          String Limit_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
          System.out.println("Value is " + Limit_Val);
          int valuetochk = Integer.parseInt(testData.get("PC_UnspecifiedItems_coverAmt").toString())
              + Integer.parseInt(testData.get("PC_specifiedItems_coverAmt").toString());
          if (Limit_Val.contains(Integer.toString(valuetochk))) {
            BoolVal = true;
            // Log.message("Both unspecified and specified cover
            // values added sucessfully,
            // added cover amount displayed as :"+valuetochk,
            // extentedReport);
          }
        }

      } else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after " + AddRem + "ing pedal cycle cover");
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val == "") {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }

  }

  /**
   * Enter Home Emergency CoverDetails
   * 
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_HEcoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
          "Unable to Switch to Windows after " + AddRem + "ing Home Emergency cover");
      String eletoInteract = RowToInteract + ":nth-child(1)>td:nth-child(3)";
      if (AddRem.equalsIgnoreCase("Add")) {
        WaitUtils.waitForElementPresent(driver, btnDashboard,
            "Unable to find the Home emergency row");
        WaitUtils.waitForElementPresent(driver, driver.findElement(By.cssSelector(eletoInteract)),
            "Unable to find Home Emergency cover on GetQuote page");

        String Limit_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + Limit_Val);
        if (Limit_Val.contains(testData.get("AddOn_HE").toString())) {
          BoolVal = true;
        }
      }

      else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after " + AddRem + "ing personal items cover");
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val.equals(" ")) {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }

  }

  /**
   * Enter Legal expenses CoverDetails
   *
   * @return boolean
   * @throws Exception
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   */
  public boolean enter_LEcoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
          "Unable to Switch to Windows after " + AddRem + "ing Legal expenses cover");
      String elementtoInteract = RowToInteract + ":nth-child(2)>td:nth-child(3)";
      if (AddRem.equalsIgnoreCase("Add")) {
        WaitUtils.waitForElementPresent(driver, btnDashboard,
            "Unable to find the Legal emergency row");

        WaitUtils.waitForElementPresent(driver,
            driver.findElement(By.cssSelector(elementtoInteract)),
            "Unable to find Legan Expenses cover on GetQuote page");
        String Limit_Val_LE = driver.findElement(By.cssSelector(elementtoInteract)).getText();
        System.out.println("Value is " + Limit_Val_LE);
        if (Limit_Val_LE.contains(testData.get("AddOn_LegalExp").toString())) {
          BoolVal = true;
        }

      } else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after " + AddRem + "ing personal items cover");
        String excess_Val = driver.findElement(By.cssSelector(elementtoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val == "") {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception("Unable to " + AddRem + " " + coverType
          + "cover details. Exception occured: " + e.getMessage());
    }

  }

  /**
   * To Click the Save button After Quote Terms
   *
   * @param extentedReport
   * @throws Exception
   * @return CustDashboardPage
   * 
   */

  public CustDashboardPage clickSaveAfterQuoteTerms(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find save button on new quote page"))
              .until(ExpectedConditions.elementToBeClickable(btn_SaveQuoteAfterTerms));
      btn_SaveQuoteAfterTerms.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on save button successfully", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on save Button" + e);
    }
    return new CustDashboardPage(driver, extentedReport).get();
  }

  /**
   * select radio button -generic
   *
   * @throws Exception
   * @param locator : string
   * @param option : string
   * 
   */
  private void selectRadioButton(String locator, String option) throws Exception {
    try {
      List<WebElement> buttonOptions = driver.findElements(By.cssSelector(locator));

      for (int i = 0; i < buttonOptions.size(); i++) {
        radio_button_value = buttonOptions.get(i).getText();
        if (radio_button_value.contains(option)) {
          WaitUtils.waitForElement(driver, buttonOptions.get(i), 2);
          buttonOptions.get(i).click();
          /*
           * Actions actions = new Actions(driver);
           * actions.moveToElement(buttonOptions.get(i)).click().build ().perform();
           */
          // ((JavascriptExecutor)
          // driver).executeScript("arguments[0].click();",
          // buttonOptions.get(i));
          Thread.sleep(2000);
          // radio_button_value = null;
          break;
        }
      }

    } catch (Exception e) {
      throw new Exception("Error in Selecting Gender : " + e);
    }
  }

  /**
   * select radio button -generic
   *
   * @throws Exception
   * 
   * @param option : string
   * 
   */

  public boolean selectRadioButton(String option) throws Exception {
    String locator = ".radio+input[id*='C2__QUE_3661A386F2BF3E14683219']+label>span";
    try {

      List<WebElement> buttonOptions = driver.findElements(By.cssSelector(locator));
      boolean status = false;
      for (int i = 0; i < buttonOptions.size(); i++) {
        radio_button_value = buttonOptions.get(i).getText();
        if (radio_button_value.contains(option)) {
          WaitUtils.waitForElement(driver, buttonOptions.get(i), 2);
          // Actions actions = new Actions(driver);
          // actions.moveToElement(buttonOptions.get(i)).click();
          // JavascriptExecutor executor = (JavascriptExecutor)
          // driver;
          // executor.executeScript("arguments[0].click();",buttonOptions.get(i));
          buttonOptions.get(i).click();
          status = true;
          Thread.sleep(3000);
          return status;

          // radio_button_value = null;

        }

      }
      return status;
    } catch (Exception e) {
      throw new Exception("Error in Selecting radio button : " + option + e);
    }
  }

  /**
   * validate unknown error msg
   *
   * @throw Exception : Custom Exception Message
   * @return boolean
   * 
   */
  public boolean err_MessageVald() throws Exception {
    boolean boolvalue = false;

    try {
      List<WebElement> errmessages = driver.findElements(By.cssSelector(txtmsg_unknwn_error));
      if (errmessages.size() != 0) {
        boolvalue = true;
      }
      return boolvalue;
    } catch (Exception e) {
      throw new Exception("Exception occured while checking for error message on quote Page" + e);
    }

  }

  /**
   * To check whether interested party field visible on newquote page
   * 
   * @throw Exception : Custom Exception Message
   * 
   */

  public boolean checkInterestedParties() throws Exception {
    return GenericUtils.verifyWebElementTextEquals(txt_interestedParties, "Interested Parties");

  }

  /**
   * To select Add interested party
   * 
   * @param extentReport
   * @param Screenshot
   * @throw Exception : Custom Exception Message
   * 
   */

  public void clickAddParties(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {

      WaitUtils.waitForElementPresent(driver, 180, addInterestedPartyBtn,
          "AddParties button is not visible");
      addInterestedPartyBtn.sendKeys(Keys.ENTER);

      Thread.sleep(2000);
      Log.message("Clicked on Add parties button", driver, extentReport, Screenshot);
      if (WaitUtils.waitForElement(driver, modal_IntParty))
        Log.message("Add interested party modal opened after clicking Add Interested party",
            extentReport);

    } catch (Exception e) {
      throw new Exception("Error in checking interested parties" + e);
    }
  }

  /**
   * To add input to the interested party window
   *
   * @param extentReport
   * @param Screenshot
   * @param teseData
   * @throw Exception : Custom Exception Message
   * 
   */

  public void Select_InterstedParty(HashMap<String, String> testData, ExtentTest extentReport,
      boolean Screenshot) throws Exception {
    WaitUtils.waitForElementPresent(driver, 180, modal_IntParty,
        "Interested party pop up has failed to open");
    try {
      if (dropdown_IntParty.getText().contains("Please select"))
        Log.message("Has the default value as 'Please select' in contact type drop down field",
            extentReport);
      else
        Log.fail("Failed to have the default value 'Please select' in contact type drop down field",
            extentReport);

      Select intParty_Cont = new Select(dropdown_IntParty);
      intParty_Cont.selectByValue((testData.get("InterestedParty")));
      WaitUtils.waitForSpinner(driver);

      Log.message("Selected contact type for interested party :" + testData.get("InterestedParty"),
          extentReport);

      if (testData.get("InterestedParty").equalsIgnoreCase("1")) {

        WaitUtils.waitForelementToBeClickable(driver, txt_serch_fName,
            "Failed to dispaly First name field on Interested party pop up window");

        AIenterLastName(testData.get("Last Name"), extentReport);
        AIenterFirstName(testData.get("First Name"), extentReport);

        WaitUtils.waitForelementToBeClickable(driver, but_Search,
            "Failed to dispaly Search button in clickable position");
        but_Search.click();

        WaitUtils.waitForelementToBeClickable(driver, but_Search,
            "Interested party window was not enabled after clicking Search button");
        WaitUtils.waitForinvisiblityofElement(driver, 40, txt_Nosearchresultdisp,
            "No contact displayed to select for interested party");

        List<WebElement> table_IP = driver.findElements(By.cssSelector(table_intPartyTR));
        String str_UsrNam =
            table_IP.get(0).findElements(By.cssSelector("td>div>span")).get(0).getText();
        if (str_UsrNam.toLowerCase().contains(testData.get("IP_Search_fName").toLowerCase())
            && table_IP.get(0).findElement(By.tagName("Button")).isEnabled())
          Log.message("Select button displayed as expected", extentReport);
        else
          Log.message("Select button not displayed as expected", extentReport);
        table_IP.get(0).findElement(By.tagName("Button")).sendKeys(Keys.ENTER);

        WaitUtils.waitForElementPresent(driver, 180, pane_IntPart,
            "Interested party : 'You are about to add' did not show on clicking select button");

        WaitUtils.waitForElementPresent(driver, 180, disp_txt_cusName,
            "Interested party window failed to have the customer name field");
        WaitUtils.waitForElementPresent(driver, 180, disp_txt_cusDOB,
            "Interested party  window failed to have the DOB field");
        Log.message("Name and DOB fields displayed as expected ", extentReport);

      } else {

        if (txt_IntPartCorp_lName.isDisplayed() && txt_IntPartCopr_Pcode.isDisplayed())

          Log.message(
              "Both search for contact field and post code  displayed for corporate contact Int Party",
              extentReport);
        else
          Log.fail(
              "Failed to display contact field and post code fields for corporate contact Int Party",
              extentReport);

      }

    } catch (Exception e) {
      throw new Exception("Exception occured while adding intersparty details" + e);
    }

  }

  /**
   * To validate the warning message on interesd party window
   *
   * @param extentReport
   * @param Screenshot
   * @throw Exception : Custom Exception Message
   * 
   */

  public void validatemsgonIntPar(ExtentTest extentReport, boolean Screenshot) throws Exception {
    WaitUtils.waitForElementPresent(driver, 180, txtmsg_warnIP,
        "Warning message field was not found on interested party window");
    try {
      if (txtmsg_warnIP.getText().equalsIgnoreCase(
          "To ensure you are adding the correct  Interested Party , Please identify  and verify the Contact prior to adding them to the Policy"))
        ;
      Log.message("Interesparty window has the expected warning message displayed as: "
          + txtmsg_warnIP.getText(), extentReport);
    } catch (Exception e) {
      throw new Exception(
          "Exception occured while validating the warning message on interested party window" + e);
    }

  }

  /**
   * To input values(from/to)of interesparty after selecting the party from search results
   * 
   * @param extentReport
   * @param Screenshot
   * @param boolEnterDate
   * @throw Exception : Custom Exception Message
   * 
   */

  public void EnterIntPartype(ExtentTest extentReport, boolean boolEnterDate, boolean Screenshot)
      throws Exception {

    WaitUtils.waitForElementPresent(driver, 180, selIntPartyType, "Unable to add Intersparty type");
    Select intParty_Cont = new Select(selIntPartyType);
    intParty_Cont.selectByValue("1");

    /*
     * SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy"); String strDate =
     * formDate.format(new Date()); Thread.sleep(2000);
     * WaitUtils.waitForelementToBeClickable(driver, datePicker_To,
     * "Unable to enter date in To date field"); if (boolEnterDate) datePicker_To.sendKeys(strDate);
     * datePicker_To.click();
     */

  }

  /**
   * To check interested party modal panes search results
   * 
   * @param extentReport
   * @param Screenshot
   * @throw Exception : Custom Exception Message
   */

  public void checkAddPartyModalPanes(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);

      WebElement addPartyPane = driver.findElement(By.cssSelector(addInterestPartyModalPane));
      WebElement searchPane = driver.findElement(By.cssSelector(addInterestSearchResultPane));

      if (addPartyPane.isDisplayed() && searchPane.isDisplayed())
        Log.message("Both Add Interested party pane and Search pane present ", driver,
            extentReport);
      else
        Log.fail("Both Add Interested party pane and Search pane are not present on pop up", driver,
            extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to check interested party panes" + e);
    }

  }

  /**
   * To check the Interested party modal pane fields
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */

  public void checkAddPartymodalPanefields(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);

      Thread.sleep(1000);

      WebElement expectedWebElement1 = driver.findElement(By.cssSelector(addInterestContactType));

      WebElement expectedWebElement3 =
          driver.findElement(By.cssSelector(addInterestsearchforContact));
      if (expectedWebElement1.isDisplayed() && expectedWebElement3.isDisplayed()
          && but_IP_cancel.isDisplayed())

        Log.message(
            "Add Interested Party window has the expected fields : Contact type and Cancel button",
            driver, extentReport, true);
      else
        Log.fail(
            "Add Interested Party window failed to have the expected fields: Contact type and Cancel button",
            driver, extentReport, true);

    } catch (Exception e) {
      throw new Exception("Unable to check interested parties" + e);
    }
  }

  /**
   * To check the Interested party search field
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void checkAddPartySearchResultsPane(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {

      WebElement createbtn = driver.findElement(By.cssSelector(addInterestCreateAddInterest));

      if (WaitUtils.waitForElement(driver, createbtn))
        Log.message("Create add interested party button is present", driver, extentReport);
      WebElement cancelbtn = driver.findElement(By.cssSelector(addInterestCancelBtn));
      Log.message(cancelbtn.getText() + " button is present", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to check interested parties" + e);
    }
  }

  /**
   * select ContactType
   * 
   * @param extentReport
   * @param contacttype : String
   * @throws Exception
   * 
   */
  public void selectContactType(String contactType, ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForelementToBeClickable(driver, addInterestcontactType,
          "Failed to locate select scheme field on new quote pop up");

      addInterestcontactType.sendKeys("Personal Contact");
      Thread.sleep(5000);
      Log.message("ContactType Selected : " + contactType, driver, extentReport);
    } catch (Exception e) {

      throw new Exception("Unable to Select scheme" + e);

    }
  }

  /**
   * To Enter first name on interested party window
   * 
   * @param extentReport
   * @param Firstname
   * @throws Exception
   * 
   */

  public void AIenterFirstName(String Firstname, ExtentTest extentReport) throws Exception {
    try {
      // WaitUtils.waitForElement(driver, addInterestFirstname);
      WaitUtils.waitForelementToBeClickable(driver, addInterestFirstname,
          "Interested party first name field was not clickable");

      // ((JavascriptExecutor) driver).executeScript("addInterestFirstname.value ='Firstname'");
      addInterestFirstname.click();
      addInterestFirstname.sendKeys(Firstname);
      // addInterestFirstname.clear();
      Thread.sleep(2000);
      Log.message("Entered the addInterest First Name : " + Firstname, driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter firstname" + e);
    }
  }

  /**
   * To Enter last name on interested party window
   * 
   * @param extentReport
   * @param Lastname
   * @throws Exception
   * 
   */

  public void AIenterLastName(String Lastname, ExtentTest extentReport) throws Exception {
    try {
      Thread.sleep(5000);
      WaitUtils.waitForElement(driver, addInterestLastname);
      addInterestLastname.click();
      addInterestLastname.sendKeys(Lastname);

      Log.message("Entered the addInterest Last Name : " + Lastname, driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter Lastname" + e);
    }
  }

  /**
   * To Click search on interested party window
   * 
   * @param extentReport
   * @throws Exception
   */
  public void clicksearch(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, addInterestsearchBtn, "Unable to click search");
      addInterestsearchBtn.click();
      Log.message("Clicked on search button", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to click search" + e);
    }

  }

  /**
   * To Click select on interested party window
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void clickselect(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, addInterestselecthBtn,
          "Unable to click select");
      addInterestselecthBtn.click();
      Log.message("Selected contact", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click select contact" + e);
    }

  }

  /**
   * To add interested party type
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */

  public void addInterestedPartyType(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, addInterestpartytype,
          "Unable to select interestedPartyType");
      addInterestpartytype.sendKeys("Hire Purchase Agreement");
      Log.message("Selected party : Underwriter", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to select party" + e);
    }

  }

  /**
   * To enter interested party to date
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */

  public void addInterestedPartyTypeToDate(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, addInterestpartytypeTodate,
          "Unable to enter To date");
      String oneyrlater = "";
      Date date = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.add(Calendar.DAY_OF_YEAR, +360);
      Date oneyr = cal.getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      oneyrlater = formatter.format(oneyr);
      addInterestpartytypeTodate.sendKeys(oneyrlater);
      Log.message("Date entered  :" + oneyrlater, driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to click select contact" + e);
    }

  }

  /**
   * To Click save interested party window
   * 
   * @param extentReport
   * @throws Exception
   * @param Screenshot :boolean
   */
  public void addInterestedPartyClickSave(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {
      Thread.sleep(2000);
      WaitUtils.waitForelementToBeClickable(driver, addInterestpartySave, "Unable to click save");
      addInterestpartySave.click();
      Log.message("Clicked on save button", driver, extentReport, true);
      Thread.sleep(8000);
    } catch (Exception e) {
      throw new Exception("Unable to click Save button " + e);
    }

  }

  /**
   * To verify delete icon on price presentation page in interested party section
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */

  public void click_Deleteicon(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, 360, addPartyDeleteicon,
          "Delete icon is not present");
      addPartyDeleteicon.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on delete icon", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click delete icon" + e);
    }

  }

  /**
   * To validate the pop up modal when bin icon clicked in interested party
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   */
  public void verifyRemoveModal(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, deletepopupmodal,
          "Remove warning popup has not opened");
      Log.message("Remove popup has opened", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to verify remove modal" + e);
    }

  }

  /**
   * To Verify MTA Status
   * 
   * @return boolean
   * @throws Exception
   * 
   */

  public boolean verifyMTAStatusEven() throws Exception {
    if (GenericUtils.verifyListInAlphabeticalOrder(textMTAStatusEven)) {
      return false;
    }
    return true;
  }

  /**
   * To validate the pop up modal when bin icon clicked in interested party
   * 
   * @param extentReport
   * @return String
   * @throws Exception
   * 
   */

  public String getPolicyPremiumAmount(ExtentTest extentedReport) throws Exception {
    WaitUtils.waitForElement(driver, txtPolicyPremiumAmount, 20);
    String textAmount = txtPolicyPremiumAmount.getAttribute("value");
    Log.message("Getting value from premium textbox", driver, extentedReport);
    return textAmount;
  }

  /**
   * To verify Policy Premium Amount
   * 
   * @param textToVerify
   * @return boolean
   * @throws Exception
   * 
   */

  public boolean verifyPolicyPremiumAmount(String textToVerify) throws Exception {
    if (!GenericUtils.verifyWebElementTextContains(txtPolicyPremiumAmount, textToVerify)) {
      return false;
    }
    return true;
  }

  /**
   * To verify by entering Tomorrow date for MTA
   * 
   * @param extentedReport
   * @return String
   * @throws Exception
   * 
   */

  public String enterTomoDateForMTA(ExtentTest extentedReport) throws Exception {
    try {
      SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy");
      String strDate = formDate.format(new Date());
      Calendar c = Calendar.getInstance();
      c.setTime(formDate.parse(strDate));
      c.add(Calendar.DATE, 1);
      strDate = formDate.format(c.getTime());
      txtMidTermDate.clear();
      txtMidTermDate.sendKeys(strDate);
      txtMidTermDate.click();
      Thread.sleep(2000);
      Log.message("Entered the Date : " + strDate, " - " + extentedReport);
      return strDate;
    }

    catch (Exception e) {
      throw new Exception("Date is not entered", e);
    }
  }

  /**
   * To verify Reverse Transaction Effective Date
   * 
   * @param textToVerify
   * @return boolean
   * @throws Exception
   * 
   */

  public boolean verifyRevTransacEffDate(String textToVerify) throws Exception {
    if (!GenericUtils.verifyWebElementTextContains(txtRevTransactionEffDate, textToVerify)) {
      return false;
    }
    return true;
  }

  /**
   * To Select Garder Cover Limit
   * 
   * @param GarderCoverLimit
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void selectGardenCoverLimit(String GardenCoverLimit, ExtentTest extentedReport)
      throws Exception {
    try {

      Select titledrpDown = new Select(cmbGardenCoverLimit);
      titledrpDown.selectByVisibleText(GardenCoverLimit);
      Log.event("Limit Selected: " + GardenCoverLimit);
      Thread.sleep(2000);
      Log.message("Selected the Limit : " + GardenCoverLimit, " - " + extentedReport);
    } catch (Exception e) {
      throw new Exception("Limit is not entered" + e);
    }
  }

  /**
   * To Add Garder Cover
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void clickAddGardenCover(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Add button"))
              .until(ExpectedConditions.elementToBeClickable(btnGardenCoverAdd));
      btnGardenCoverAdd.click();
      Log.message("Clicked on Add button", driver, extentedReport, true);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Add Button" + e);
    }
  }

  /**
   * To Click Save Garden Cover
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void clickSaveGardenCover(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find save button"))
              .until(ExpectedConditions.elementToBeClickable(btnGardenCoverSave));
      btnGardenCoverSave.click();
      Log.message("Clicked on save button", driver, extentedReport, true);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on save Button" + e);
    }
  }

  /**
   * To Select Policy Cancel Reason
   * 
   * @param PolicyCancelReason
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void selectPolicyCancelReason(String PolicyCancelReason, ExtentTest extentedReport)
      throws Exception {
    try {

      Select titledrpDown = new Select(cmbCancelPolicyReason);
      titledrpDown.selectByVisibleText(PolicyCancelReason);
      Log.event("Cancel Reason Selected: " + PolicyCancelReason);
      Thread.sleep(2000);
      Log.message("Selected the Reason : " + PolicyCancelReason, " - " + extentedReport);
    } catch (Exception e) {
      throw new Exception("Reason is not entered" + e);
    }
  }

  /**
   * To Click Cancel on Policy Calculate
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void clickCancelPolicyCalculate(ExtentTest extentedReport) throws Exception {
    try {
      btnCancelPolicyCalculate.click();
      Thread.sleep(2000);
      Log.event("Calculate return button clicked" + extentedReport);
    } catch (Exception e) {
      throw new Exception("Calculate return button not clicked", e);
    }
  }

  /**
   * To Select Policy Cancel Return
   * 
   * @param PolicyCancelReturn
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void selectPolicyCancelReturn(String PolicyCancelReturn, ExtentTest extentedReport)
      throws Exception {
    try {

      Select titledrpDown = new Select(cmbCancelPolicyReturn);
      titledrpDown.selectByVisibleText(PolicyCancelReturn);
      Log.event("Cancel return Selected: " + PolicyCancelReturn);
      Thread.sleep(2000);
      Log.message("Selected the return : " + PolicyCancelReturn, " - " + extentedReport);
    } catch (Exception e) {
      throw new Exception("Return is not entered" + e);
    }
  }

  /**
   * To Click Standard Cancellation
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void clickStandardCancelletion(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, drpoptStandardCancel);
      drpoptStandardCancel.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Standard Cancelletion policy", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on standard policy cancel Button" + e);
    }
  }

  /**
   * To Enter Policy Cancel Date
   * 
   * @throws Exception
   * 
   */

  public void enterPolicyCancelDate() throws Exception {
    try {
      SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy");
      String strDate = formDate.format(new Date());
      Thread.sleep(2000);
      txtCancelPolicyDate.sendKeys(strDate);
      txtCancelPolicyDate.click();
      Thread.sleep(2000);
      Log.event("Entered the Date : " + strDate);
    }

    catch (Exception e) {
      throw new Exception("Date of Birth is not entered" + e);
    }
  }

  /**
   * To Click on Cancel Policy
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void clickCancelPolicy(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Cancel Policy"))
              .until(ExpectedConditions.elementToBeClickable(drpCancelPolicy));
      drpCancelPolicy.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Cancel Policy after upgrading policy", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Cancel Policy Button" + e);
    }
  }

  /**
   * To check the remove description when bin icon clicked in interested party
   * 
   * @throws Exception
   * 
   */

  public boolean checkremoveDescription() throws Exception {
    boolean status = false;
    List<WebElement> description = driver.findElements(By.cssSelector(removeDescription));
    if (description.get(0).getText().contains(INTERESTED_PARTIES_REMOVE_DESC1)
        && description.get(1).getText().equals(INTERESTED_PARTIES_REMOVE_DESC2)) {
      status = true;
    }
    return status;
  }

  /**
   * To check the yes /no button on remove description when bin icon clicked in interested party
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   */

  public void yesnobuttonVisible(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, yesbtn, "Yes button not visible");
      Log.message("yes button is visible", driver, extentReport);
      WaitUtils.waitForelementToBeClickable(driver, nobtn, "No button not visible");
      Log.message("No button is visible", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to click yes button" + e);
    }
  }

  /**
   * To click yes button when bin icon clicked in interested party
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   */

  public void clickyesbutton(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {

      WaitUtils.waitForelementToBeClickable(driver, yesbtn, "Yes button not visible");
      yesbtn.click();
      Log.message("Clicked on yes button", driver, extentReport, true);
      WaitUtils.waitForSpinner(driver);
      if (!WaitUtils.waitForElement(driver, yesbtn, 2))
        Log.message("Remove description window closed after clicking Yes button", extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to click yes button" + e);
    }
  }

  /**
   * To click no button when bin icon clicked in interested party
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   */

  public void clicknobutton(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, nobtn, "No button not visible");
      nobtn.click();
      Log.message("Clicked on NO button", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click yes button" + e);
    }
  }

  /**
   * To click close button when bin icon clicked in interested party
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   */
  public void clickClosebutton(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, closebtn, "No button not visible");
      closebtn.click();
      Log.message("Clicked on Close button", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click yes button" + e);
    }
  }

  /**
   * To click create new interested party
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   */



  public void clickcreatenewAddparty(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      /*
       * WaitUtils.waitForelementToBeClickable(driver, addInterestcreateAddInterest,
       * "CreateAddinterest button is not visible");
       */
      WaitUtils.waitForElement(driver, addInterestcreateAddInterestbutton);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", addInterestcreateAddInterestbutton);
      // addInterestcreateAddInterest.sendKeys(Keys.ENTER);
      Log.message("Clicked on create New Add Interest button", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click New Add Interest button" + e);
    }
  }

  /**
   * To click Save button in Quote
   * 
   * @throws Exception
   * @param typeOfQuote
   * @param extentedReport
   * @param Screenshot :boolean
   */

  public void clickQuoteSave(String typeOfQuote, String txtToenter, ExtentTest extentedReport)
      throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);

      switch (typeOfQuote) {

        case "NB":
          btnClickQuoteSave.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Save quote", driver, extentedReport);
          if (WaitUtils.waitForElement(driver, btnContinueSaveQuote)) {
            fldEmailSaveQuote.clear();
            Actions actions = new Actions(driver);
            actions.moveToElement(fldEmailSaveQuote).click();
            actions.sendKeys(txtToenter);
            Thread.sleep(2000);
            actions.sendKeys(Keys.RETURN).build().perform();
            // fldEmailSaveQuote.sendKeys(txtToenter);
            btnContinueSaveQuote.click();
            Log.message("Entered Email address and clicked on Continue button : " + txtToenter,
                driver, extentedReport);
            Thread.sleep(2000);
            WaitUtils.waitForSpinner(driver);
          }
          break;

        case "Renew":
          WaitUtils.waitForElementPresent(driver, btn_renewQuoteSave,
              "Save button not found,failed to save renewal quote");
          btn_renewQuoteSave.click();
          Log.message("Clicked on save button - Policy Renewal transaction", extentedReport);
          WaitUtils.waitForElementPresent(driver, txtFld_quoteDesc,
              "Quote variation modal failed to open /unable to locate quote variation description field");

          Actions actions = new Actions(driver);
          actions.moveToElement(txtFld_quoteDesc).click();
          actions.sendKeys(txtToenter);
          Thread.sleep(2000);
          actions.sendKeys(Keys.RETURN).build().perform();

          Log.message("Entered quote description :" + txtToenter, extentedReport);
          if (WaitUtils.waitForElement(driver, btn_qutCancel, 3))
            Log.message("Cancel button present in quote description modal", extentedReport);

          WaitUtils.waitForElementPresent(driver, btn_saveQuoteDesc,
              "Failed to locate quote variation save button on quote description modal");
          btn_saveQuoteDesc.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on save button on quote description modal", extentedReport);

          break;
      }
    } catch (Exception e) {
      throw new Exception("Unable to click on Save quote Button :" + e);
    }
  }

  /**
   * Writes the given customer details in the 'Create New Customer' form.<br/>
   * <br/>
   * The vararg '<i>shouldForceRandomNameCreation</i>' of the method is used to tell whether the
   * random name (both first name and last name) should be entered in form despite the name value
   * given in <i>testData<i/>.
   * 
   * @param testData
   * @param screenShot
   * @param extendReport
   * @param shouldForceRandomNameCreation
   * @return the <code>HashMap</code> representation of testData, or null if no <code>HashMap</code>
   *         representation is applicable
   * @throws Exception
   */
  /*
   * public HashMap<String, String> enterCustomerDetails(HashMap<String, String> testData, boolean
   * screenShot, ExtentTest extendReport, boolean... shouldForceRandomNameCreation) throws Exception
   * { try { WaitUtils.waitForElementPresent(driver, Ele_engagement_Centre_tabs,
   * "Unable to find Perosnal/corporate contact tabs");
   * 
   * if (app_BrandName.getText().equalsIgnoreCase("Engagement Centre")) {
   * WaitUtils.waitForElementPresent(driver, enga_Centre_tabs_Personal,
   * "Unable to find Personal contact tab"); enga_Centre_tabs_Personal.click(); Thread.sleep(2000);
   * }
   * 
   * boolean forceRandomValueFlag = shouldForceRandomNameCreation.length > 0 ?
   * shouldForceRandomNameCreation[0] : false; String title = testData.get("Title").toString();
   * String firstName = testData.get("First Name").toString(); String lastName =
   * testData.get("Last Name").toString(); String dob = testData.get("Date of Birth").toString();
   * String gender = testData.get("Gender").toString(); String postcode =
   * testData.get("Post Code").toString(); String homephone = testData.get("Home Phone").toString();
   * String email = testData.get("Email").toString();
   * 
   * Log.message("Entering Customer details"); selectTitle(title); Log.message("Title : " + title);
   * 
   * if (forceRandomValueFlag || firstName.equals("")) firstName =
   * GenericUtils.getRandomCharacters("alpha", 5);
   * 
   * entercn_firstName(firstName); testData.put("First Name", firstName);
   * Log.message("First Name : " + firstName, extendReport);
   * 
   * if (forceRandomValueFlag || lastName.equals("")) lastName =
   * GenericUtils.getRandomCharacters("alpha", 4);
   * 
   * entercn_LastName(lastName); testData.put("Last Name", lastName); Log.message("Last Name : " +
   * lastName, extendReport);
   * 
   * entercn_DateOfBirth(dob); Log.message("Date of Birth : " + dob, extendReport);
   * Thread.sleep(2000);
   * 
   * selectGender(gender); Log.message("Gender : " + gender, extendReport);
   * 
   * entercn_PostCode(postcode); Log.message("Post Code : " + postcode, extendReport);
   * 
   * entercn_HomePhone(homephone); Log.message("Home Phone : " + homephone, extendReport);
   * 
   * if (!email.contains("@")) email = testData.get("Email") + DateTimeUtility.getTimeIn_MMSS() +
   * "IntParty@ectest.com";
   * 
   * cn_Email.clear(); cn_Email.sendKeys(email);
   * 
   * Log.message("Email : " + email, extendReport); testData.put("Email", email);
   * 
   * selectRadioButton(radPreferences, "Email");
   * 
   * Log.message("Sucessfully entered the Customer details", driver, extendReport, screenShot);
   * 
   * } catch (Exception e) { throw new Exception("Exception entering the customer details. " + e); }
   * return testData; }
   */

  public HashMap<String, String> enterCustomerDetails(HashMap<String, String> testData,
      boolean screenShot, ExtentTest extendReport, boolean... shouldForceRandomNameCreation)
      throws Exception {
    try {

      boolean forceRandomValueFlag =
          shouldForceRandomNameCreation.length > 0 ? shouldForceRandomNameCreation[0] : false;
      String title = testData.get("Title").toString();
      String firstName = testData.get("First Name").toString();
      String lastName = testData.get("Last Name").toString();
      String dob = testData.get("Date of Birth").toString();
      String gender = testData.get("Gender").toString();
      String postcode = testData.get("Post Code").toString();
      String homephone = testData.get("Home Phone").toString();
      String email = testData.get("Email").toString();

      Log.message("Entering Customer details");
      selectTitle(title);
      Log.message("Title : " + title);

      if (forceRandomValueFlag || firstName.equals(""))
        firstName = GenericUtils.getRandomCharacters("alpha", 5);

      entercn_firstName(firstName);
      testData.put("First Name", firstName);
      Log.message("First Name : " + firstName, extendReport);

      if (forceRandomValueFlag || lastName.equals(""))
        lastName = GenericUtils.getRandomCharacters("alpha", 4);

      entercn_LastName(lastName);
      testData.put("Last Name", lastName);
      Log.message("Last Name : " + lastName, extendReport);

      entercn_DateOfBirth(dob);
      Log.message("Date of Birth : " + dob, extendReport);

      selectGender(gender);
      Log.message("Gender : " + gender, extendReport);

      entercn_PostCode(postcode);
      Log.message("Post Code : " + postcode, extendReport);

      selectAddress(extendReport); // this feature of selectingf the
      // address is changed
      // To handle an AJAX request which is processed after the selection
      // of 'Find
      // Address'

      entercn_HomePhone(homephone);
      Log.message("Home Phone : " + homephone, extendReport);

      if (!email.contains("@"))
        email =
            testData.get("Email") + DateTimeUtility.getCurrentDateAndTime() + "SIAAS@ectest.com";

      cn_Email.clear();
      WaitUtils.waitForElement(driver, cn_Email);
      cn_Email.click();
      cn_Email.sendKeys(email);

      Log.message("Email : " + email, extendReport);
      testData.put("Email", email);

      selectRadioButton(radPreferences, "Email");

      Log.message("Sucessfully entered the Customer details", driver, extendReport, screenShot);

    } catch (Exception e) {
      throw new Exception("Exception entering the customer details. " + e);
    }
    return testData;
  }


  public HashMap<String, String> enterIntrestedpartyCustomerDetails(
      HashMap<String, String> testData, boolean screenShot, ExtentTest extendReport,
      boolean... shouldForceRandomNameCreation) throws Exception {
    try {

      boolean forceRandomValueFlag =
          shouldForceRandomNameCreation.length > 0 ? shouldForceRandomNameCreation[0] : false;
      String title = testData.get("Title").toString();
      String firstName = testData.get("IPCustFirstName").toString();
      String lastName = testData.get("IPCustLastName").toString();
      String dob = testData.get("Date of Birth").toString();
      String gender = testData.get("Gender").toString();
      String postcode = testData.get("Post Code").toString();
      String homephone = testData.get("Home Phone").toString();
      String email = testData.get("Email").toString();

      Log.message("Entering Customer details");
      selectTitle(title);
      Log.message("Title : " + title);

      if (forceRandomValueFlag || firstName.equals(""))
        firstName = GenericUtils.getRandomCharacters("alpha", 5);

      entercn_firstName(firstName);
      testData.put("First Name", firstName);
      Log.message("First Name : " + firstName, extendReport);

      if (forceRandomValueFlag || lastName.equals(""))
        lastName = GenericUtils.getRandomCharacters("alpha", 4);

      entercn_LastName(lastName);
      testData.put("Last Name", lastName);
      Log.message("Last Name : " + lastName, extendReport);

      entercn_DateOfBirth(dob);
      Log.message("Date of Birth : " + dob, extendReport);

      selectGender(gender);
      Log.message("Gender : " + gender, extendReport);

      entercn_PostCode(postcode);
      Log.message("Post Code : " + postcode, extendReport);

      selectAddress(extendReport); // this feature of selectingf the
      // address is changed
      // To handle an AJAX request which is processed after the selection
      // of 'Find
      // Address'

      entercn_HomePhone(homephone);
      Log.message("Home Phone : " + homephone, extendReport);

      if (!email.contains("@"))
        email =
            testData.get("Email") + DateTimeUtility.getCurrentDateAndTime() + "SIAAS@ectest.com";

      cn_Email.clear();
      WaitUtils.waitForElement(driver, cn_Email);
      cn_Email.click();
      cn_Email.sendKeys(email);

      Log.message("Email : " + email, extendReport);
      testData.put("Email", email);

      selectRadioButton(radPreferences, "Email");

      Log.message("Sucessfully entered the Customer details", driver, extendReport, screenShot);

    } catch (Exception e) {
      throw new Exception("Exception entering the customer details. " + e);
    }
    return testData;
  }

  /**
   * To select title
   * 
   * @throws Exception
   * @param Title :String
   */

  public void selectTitle(String title) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, selecttitle, "Title field is not present");
      Select titledrpDown = new Select(selecttitle);
      titledrpDown.selectByVisibleText(title);
      Log.message("Title Selected: " + title);
      WaitUtils.waitForSpinner(driver);

    }

    catch (Exception e) {
      throw new Exception("Title is not selected" + e);
    }
  }

  /**
   * To enter first name
   * 
   * @throws Exception
   * @param firstname :String
   */

  public void entercn_firstName(String cn_firstname) throws Exception {
    try {
      cn_firstName.clear();
      cn_firstName.sendKeys(cn_firstname);
      Log.message("Entered the First Name: " + cn_firstname);
    } catch (Exception e) {
      throw new Exception("First Name is not entered" + e);
    }

  }

  /**
   * To enter last name
   * 
   * @throws Exception
   * @param lastname :String
   */

  public void entercn_LastName(String cn_lastname) throws Exception {
    try {
      cn_lastName.clear();
      cn_lastName.sendKeys(cn_lastname);
      Log.message("Entered the Last Name: " + cn_lastname);
    } catch (Exception e) {
      throw new Exception("Last Name is not entered" + e);
    }
  }

  public boolean verifyPaymentHub(WebDriver driver) {
    driver.switchTo().frame(frame_sagepay);
    WaitUtils.waitForElement(driver, paymentHub_title);

    boolean status = true;
    if (paymentHub_title.getText().equalsIgnoreCase("Secure Payment Page")) {
      Log.message("User is navigated to Payment Hub");
    }

    else {
      status = false;
      Log.message("User is not navigated to Payment Hub");
    }
    return status;

  }

  /**
   * To enter DOB
   * 
   * @throws Exception
   * @param cn_dateOfBirth :String
   */

  public void entercn_DateOfBirth(String cn_dateOfBirth) throws Exception {
    try {
      cn_dateofbirth.clear();
      Actions actions = new Actions(driver);
      actions.moveToElement(cn_dateofbirth).click();
      actions.sendKeys(cn_dateOfBirth);
      Thread.sleep(2000);
      actions.sendKeys(Keys.RETURN).build().perform();
      //
      //
      // cn_dateofbirth.sendKeys(cn_dateOfBirth);
      // cn_dateofbirth.click();
      // Thread.sleep(2000);
      Log.event("Entered the Date of Birth: " + cn_dateOfBirth);
    }

    catch (Exception e) {
      throw new Exception("Date of Birth is not entered" + e);
    }
  }

  /**
   * To select gender
   * 
   * @throws Exception
   * @param gender :String
   */

  public void selectGender(String gender) throws Exception {

    try {
      selectRadioButton(radGender, gender);
    } catch (Exception e) {

      e.printStackTrace();
    }
    Log.event("Selected Gender :" + gender);
  }

  /**
   * To enter post code
   * 
   * @throws Exception
   * @param cn_postcode :String
   */
  public void entercn_PostCode(String cn_postcode) throws Exception {
    try {
      cn_postCode.clear();
      cn_postCode.sendKeys(cn_postcode);
      Log.message("Entered the PostCode: " + cn_postcode);
      cn_findAddress.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Find Address");
    } catch (Exception e) {
      throw new Exception("Post Code is not entered" + e);
    }
  }

  /**
   * To select address
   * 
   * @throws Exception
   * 
   */
  public void selectAddress() throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Address not Listed"))
              .until(ExpectedConditions.visibilityOf(cn_AddressList));
      Select cn_Address = new Select(cn_AddressList);
      cn_Address.selectByIndex(0);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Address is not selected"))
              .until(ExpectedConditions.visibilityOf(cn_AddressLine));
    }

    catch (Exception e) {
      throw new Exception("Unable to Select Address" + e);
    }
  }

  /**
   * To enter home phone
   * 
   * @throws Exception
   * @param cn_homephone :String
   */
  public void entercn_HomePhone(String cn_homephone) throws Exception {
    try {
      cn_HomePhone.clear();
      cn_HomePhone.sendKeys(cn_homephone);
      Log.event("Phone Number is entered " + cn_homephone);
    }

    catch (Exception e) {
      throw new Exception("Phone number is not entered" + e);
    }
  }

  /**
   * To enter entercn_Email
   * 
   * @throws Exception
   * @param cn_email :String
   */
  public void entercn_Email(String cn_email) throws Exception {
    try {
      cn_Email.clear();
      SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
      Date date = new Date();
      String time = sdf.format(date);

      cn_email = cn_email + time + "@ecTest.com";
      WaitUtils.waitForElement(driver, cn_Email);
      cn_Email.click();
      cn_Email.sendKeys(cn_email);
      Log.event("Entered Email ID: " + cn_email);
    }

    catch (Exception e) {
      throw new Exception("Email ID is not entered" + e);
    }
  }

  /**
   * Verify banner title as Engagement centre
   * 
   * @return boolean
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */


  public <Extent> boolean ECbannertitleCheck(Extent test) throws Exception {

    try {
      boolean flag = false;


      // driver.get("html/images/Brands/Aldershot Branch.png");

      WebElement image1 = driver.findElement((By.xpath("//img[contains(@alt,'Brand')]")));

      boolean flagtest = image1.isDisplayed();
      if (flagtest) {

        Log.message("Logo is displayed ");
        flag = true;
        // return GenericUtils.verifyWebElementTextEquals(txtBannerName, "SSP");
      } else {
        Log.message("Logo is not displayed ");
      }

      return flag;
    }

    catch (Exception e) {
      throw new Exception("Exception while getting banner title" + e);
    }

  }


  /**
   * To click confirm CreateCustomer
   * 
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   * @param screenShot :boolean
   */

  public void confirmCreateCustomer(boolean screenShot, ExtentTest extentedReport)
      throws Exception {
    WaitUtils.waitForSpinner(driver);
    try {
      WaitUtils.waitForElement(driver, cn_SaveCustomer);
      JavascriptExecutor executor = (JavascriptExecutor) driver;
      executor.executeScript("arguments[0].click();", cn_SaveCustomer);
      // cn_SaveCustomer.click();
      Log.message("Clicked on Save customer", driver, extentedReport, screenShot);
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForinvisiblityofElement(driver, 60, but_personSave,
          "Failed to close the new customer creationg window");

    } catch (Exception e) {

      throw new Exception("Error occured while creating customer" + e);
    }

  }

  /**
   * To Validate NewIPButon and cancel Button
   *
   * @throws Exception : Custom Exception Message
   * @param extentReport
   * @param Screenshot : boolean
   * 
   */

  public void ValidateNewIPButon_cancelBut(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    WaitUtils.waitForelementToBeClickable(driver, addInterestcreateAddInterest,
        "CreateAddinterest button is not visible");
    try {
      if (but_addIntParty_Cancel.isDisplayed() && addInterestcreateAddInterest.isDisplayed())
        Log.message(
            "Interesparty window has create New Interstedparty and cancel button on interested party window",
            extentReport);
      else
        Log.fail(
            "Interesparty window failed to display create New Interstedparty and cancel button on interested party window",
            extentReport);

      List<WebElement> Ele_cnlBut = driver.findElement(By.cssSelector(pane_IP_searchResults))
          .findElements(By.cssSelector(but_IP_searchResults_Cnl));
      if (Ele_cnlBut.size() == 1)
        Log.message("Cancel button not found beside Crete New InterestedParty button",
            extentReport);
      else
        Log.fail(
            "Cancel button found beside Crete New InterestedParty button. Investigation needed!!",
            extentReport);

    } catch (Exception e) {
      throw new Exception(
          "Exception occured while validating the cancel and add new interested party button on interested party window"
              + e);
    }

  }

  /**
   * To save interested party
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   */

  public void IntParty_butsave(ExtentTest extentReport, boolean Screenshot) throws Exception {
    WaitUtils.waitForelementToBeClickable(driver, but_IntPartysave,
        "Save button was not clicked on Interested party window");

    try {
      but_IntPartysave.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on save button successfully on Interested party window", extentReport);
      if (!WaitUtils.waitForElement(driver, but_IntPartysave, 3))
        Log.message("Interested party window closed successfully on clicking save button",
            extentReport);
    } catch (Exception e) {
      throw new Exception("Failed to click on save button :" + e);
    }
  }

  /**
   * To Validate then errMsg when No Date Set on interested party window
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   */

  public void Validate_errMsg_whenNoDateSet(ExtentTest extentReport, boolean Screenshot)
      throws Exception {

    if (txt_errMsg_noDate.getText().equalsIgnoreCase("Please enter the To date"))
      Log.message("Throws error message when to date not set", extentReport);
    else
      Log.fail("Failed to throw error message when to date not set on interested party window",
          extentReport);

  }

  /**
   * To validate corp Contact Intparty Fields
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   */
  public void validate_corpContact_IntpartyFields(ExtentTest extentReport, boolean Screenshot)
      throws Exception {

    Select intParty_Cont = new Select(dropdown_IntParty);
    intParty_Cont.selectByValue("2");
    Log.message("Selected corporate contact type as interested party", extentReport);
    WaitUtils.waitForelementToBeClickable(driver, txt_IntPartCorp_lName,
        "Failed to dispaly search for contact field on Interested party pop up window");

    if (txt_IntPartCorp_lName.getText().isEmpty() && txt_IntPartCopr_Pcode.getText().isEmpty())
      Log.message(
          "Exisiting search data getting removed when contacttype changed from personal to coporate on Intersted party window",
          extentReport);
    else
      Log.fail(
          "Exisiting search was not  removed when contacttype changed from personal to coporate on Intersted party window",
          extentReport);

  }

  /**
   * To search last Name in coporateIP
   * 
   * @throws Exception
   * @param extentReport
   * @param Screenshot :boolean
   * @param testdata :hashmap
   */
  public void search_lName_coporateIP(HashMap<?, ?> testData, ExtentTest extentReport,
      boolean Screenshot) throws Exception {
    WaitUtils.waitForSpinner(driver);
    WaitUtils.waitForElement(driver, txt_IntPartCorp_lName, 5);
    txt_IntPartCorp_lName.sendKeys(testData.get("Trade Name").toString());
    txt_IntPartCopr_Pcode.sendKeys(testData.get("Post Code").toString());

    WaitUtils.waitForelementToBeClickable(driver, but_Search_CorpIP,
        "Failed to dispaly Search button in clickable position");
    but_Search_CorpIP.click();
    WaitUtils.waitForelementToBeClickable(driver, but_Search_CorpIP,
        "Interested party window was not enabled after clicking Search button");

  }

  /**
   * To createCorporateContact
   * 
   * @throws Exception
   * @param testdata :hashmap
   * @param extentReport
   * @param Screenshot :boolean
   */

  public void createCorporateContact(HashMap<?, ?> testdata, ExtentTest extentReport,
      boolean Screenshot) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, Ele_engagement_Centre_tabs,
          "Unable to find Perosnal/corporate contact tabs");
      enga_Centre_tabs_Corporate.click();
      Thread.sleep(2000);

      Log.message("Entering Customer details");

      Thread.sleep(5000);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to Switch to Windows"))
              .until(ExpectedConditions.visibilityOf(txtTradeName));
      System.out.println("Entering Trade Details");
      enterTradeDetails(testdata);
      enterBizDetails(testdata);
      enterBTDetails(testdata);
      enterAuthContactDetails(testdata);
      enterAuthContactphoneDetails(testdata);
      Log.message("Successfully added corporate contact", extentReport);

      cn_SaveCustomer_Corp.click();
      Log.message("Clicked on Save corporate customer", driver, extentReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Error while entering corporate customer details" + e);
    }
  }

  /**
   * To enterTradeDetails
   * 
   * @throws Exception
   * @param testdata :hashMap
   */
  public void enterTradeDetails(HashMap<?, ?> testdata) throws Exception {
    try {
      System.out.println("Inside Trade");
      enterTradeName(testdata.get("Trade Name").toString());
      Log.event("Entered the Trade Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterBizDetails
   * 
   * @throws Exception
   * @param testdata :hashMap
   */

  public void enterBizDetails(HashMap<?, ?> testdata) throws Exception {
    try {

      selectType_CC(testdata.get("Type").toString());
      selectCountry_CC(testdata.get("Country").toString());
      Thread.sleep(2000);
      enterPostCode(testdata.get("PostCode").toString());
      Thread.sleep(2000);
      clickfindAddress(true);
      Thread.sleep(2000);
      selectAddressList_CC();
      Log.event("Entered the Bisuness Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterBTDetails
   * 
   * @throws Exception
   * @param testdata :hashMap
   */
  public void enterBTDetails(HashMap<?, ?> testdata) throws Exception {
    try {

      enterBTBiz(testdata.get("Phone Number").toString());
      enterBTHome(testdata.get("Phone Number").toString());
      enterBTMob(testdata.get("Phone Number").toString());
      enterBEEmail(testdata.get("Email").toString());
      Log.event("Entered the Bisuness Telephone Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterAuthContactDetails
   * 
   * @throws Exception
   * @param testdata :hashMap
   */
  public void enterAuthContactDetails(HashMap<?, ?> testdata) throws Exception {
    try {

      selectTitle_CC(testdata.get("Title").toString());
      enterFirstName(testdata.get("First Name").toString() + "Corp");
      enterLastName_cc(testdata.get("Last Name").toString() + "Corp");
      Log.event("Entered the Authorized Contact Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterAuthContactphoneDetails
   * 
   * @throws Exception
   * @param testdata :hashMap
   */
  public void enterAuthContactphoneDetails(HashMap<?, ?> testdata) throws Exception {
    try {

      enterAuthphoneHome(testdata.get("Phone Number").toString());
      enterAuthphonework(testdata.get("Phone Number").toString());
      enterAuthphoneMob(testdata.get("Phone Number").toString());
      enterAuthphoneEmail(testdata.get("Email").toString());
      Log.event("Entered the Authorized Contact Telephone Details");
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enterTradeName
   * 
   * @throws Exception
   * @param TradeName :String
   */
  public void enterTradeName(String TradeName) throws Exception {
    try {
      System.out.println("Entering Trade Name :" + TradeName);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Trade name text box"))
              .until(ExpectedConditions.visibilityOf(txtTradeName));
      txtTradeName.clear();
      txtTradeName.sendKeys(TradeName);
      Log.event("Entered the TradeName: " + TradeName);
    } catch (Exception e) {
      throw new Exception("Trade name field is not displayed" + e);
    }
  }

  /**
   * To enter enterTradeAs
   * 
   * @throws Exception
   * @param TradeAs :String
   * 
   */
  public void enterTradeAs(String TradeAs) throws Exception {
    try {
      System.out.println("Entering Trade As");
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Trade As text box"))
              .until(ExpectedConditions.visibilityOf(txtTradeAs));
      txtTradeAs.clear();
      txtTradeAs.sendKeys(TradeAs);
      Log.event("Entered the TradeAs: " + TradeAs);
    } catch (Exception e) {
      throw new Exception("TradeAs field is not displayed" + e);
    }
  }

  /**
   * To enterBuidlingnum
   * 
   * @throws Exception
   * @param BuldingNum :String
   */
  public void enterBuidlingnum(String BuldingNum) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BuldingNum text box"))
              .until(ExpectedConditions.visibilityOf(txtBuldNumber));
      txtBuldNumber.clear();
      txtBuldNumber.sendKeys(BuldingNum);
      Log.event("Entered the TradeAs: " + BuldingNum);
    } catch (Exception e) {
      throw new Exception("BuldingNum field is not displayed" + e);
    }
  }

  /**
   * To enterPostCode
   * 
   * @throws Exception
   * @param PostCode :String
   */
  public void enterPostCode(String PostCode) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find PostCode text box"))
              .until(ExpectedConditions.visibilityOf(txtPostcode));
      txtPostcode.clear();
      txtPostcode.sendKeys(PostCode);
      Log.event("Entered the PostCode: " + PostCode);
    } catch (Exception e) {
      throw new Exception("PostCode field is not displayed" + e);
    }
  }

  /**
   * To enterBTBiz
   * 
   * @throws Exception
   * @param BTBiz :String
   */
  public void enterBTBiz(String BTBiz) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BTBiz text box"))
              .until(ExpectedConditions.visibilityOf(txtBTBiz));
      txtBTBiz.clear();
      txtBTBiz.sendKeys(BTBiz);
      Log.event("Entered the BTBiz: " + BTBiz);
    } catch (Exception e) {
      throw new Exception("BTBiz field is not displayed" + e);
    }
  }

  /**
   * To enterBTHome
   * 
   * @throws Exception
   * @param BTHome :String
   */
  public void enterBTHome(String BTHome) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BTHome text box"))
              .until(ExpectedConditions.visibilityOf(txtBTHome));
      txtBTHome.clear();
      txtBTHome.sendKeys(BTHome);
      Log.event("Entered the BTHome: " + BTHome);
    } catch (Exception e) {
      throw new Exception("BTHome field is not displayed" + e);
    }
  }

  /**
   * To enterBTMob
   * 
   * @throws Exception
   * @param BTMob :String
   */
  public void enterBTMob(String BTMob) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BTMob text box"))
              .until(ExpectedConditions.visibilityOf(txtBTMob));
      txtBTMob.clear();
      txtBTMob.sendKeys(BTMob);
      Log.event("Entered the BTMob: " + BTMob);
    } catch (Exception e) {
      throw new Exception("BTMob field is not displayed" + e);
    }
  }

  /**
   * To enterBEEmail
   * 
   * @throws Exception
   * @param BEEmail :String
   */
  public void enterBEEmail(String BEEmail) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BEEmail text box"))
              .until(ExpectedConditions.visibilityOf(txtBEEmail));
      txtBEEmail.clear();
      txtBEEmail.sendKeys(BEEmail);
      Thread.sleep(10000);
      Log.event("Entered the BEEmail: " + BEEmail);
    } catch (Exception e) {
      throw new Exception("BEEmail field is not displayed" + e);
    }
  }

  /**
   * To enterBEWbsite
   * 
   * @throws Exception
   * @param BEWbsite :String
   */
  public void enterBEWbsite(String BEWbsite) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find BEWbsit text box"))
              .until(ExpectedConditions.visibilityOf(txtBEWbsite));
      txtBEWbsite.clear();
      txtBEWbsite.sendKeys(BEWbsite);
      Log.event("Entered the BEWbsite: " + BEWbsite);
    } catch (Exception e) {
      throw new Exception("BEWbsite field is not displayed" + e);
    }
  }

  /**
   * To enterAuthphoneHome
   * 
   * @throws Exception
   * @param AuthphoneHome :String
   */
  public void enterAuthphoneHome(String AuthphoneHome) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find AuthphoneHome text box"))
              .until(ExpectedConditions.visibilityOf(txtAuthphoneHome));
      txtAuthphoneHome.clear();
      txtAuthphoneHome.sendKeys(AuthphoneHome);
      Log.event("Entered the AuthphoneHome: " + AuthphoneHome);
    } catch (Exception e) {
      throw new Exception("AuthphoneHome field is not displayed" + e);
    }
  }

  /**
   * To enterAuthphonework
   * 
   * @throws Exception
   * @param Authphonework :String
   */
  public void enterAuthphonework(String Authphonework) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Authphonework text box"))
              .until(ExpectedConditions.visibilityOf(txtAuthphonework));
      txtAuthphonework.clear();
      txtAuthphonework.sendKeys(Authphonework);
      Log.event("Entered the Authphonework: " + Authphonework);
    } catch (Exception e) {
      throw new Exception("Authphonework field is not displayed" + e);
    }
  }

  /**
   * To enterAuthphoneMob
   * 
   * @throws Exception
   * @param AuthphoneMob :String
   */
  public void enterAuthphoneMob(String AuthphoneMob) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find AuthphoneMob text box"))
              .until(ExpectedConditions.visibilityOf(txtAuthphoneMob));
      txtAuthphoneMob.clear();
      txtAuthphoneMob.sendKeys(AuthphoneMob);
      Log.event("Entered the AuthphoneMob: " + AuthphoneMob);
    } catch (Exception e) {
      throw new Exception("AuthphoneMob field is not displayed" + e);
    }
  }

  /**
   * To enterAuthphoneEmail
   * 
   * @throws Exception
   * @param AuthphoneEmail :String
   */
  public void enterAuthphoneEmail(String AuthphoneEmail) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find AuthphoneEmail text box"))
              .until(ExpectedConditions.visibilityOf(txtAuthphoneEmail));
      txtAuthphoneEmail.clear();
      txtAuthphoneEmail.sendKeys(AuthphoneEmail);
      Log.event("Entered the AuthphoneEmail: " + AuthphoneEmail);
    } catch (Exception e) {
      throw new Exception("AuthphoneEmail field is not displayed" + e);
    }
  }

  /**
   * To enterFirstName
   * 
   * @throws Exception
   * @param FirstName :String
   */
  public void enterFirstName(String FirstName) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find FirstName text box"))
              .until(ExpectedConditions.visibilityOf(txtFirstName));
      txtFirstName.clear();
      txtFirstName.sendKeys(FirstName);
      Log.event("Entered the FirstName: " + FirstName);
    } catch (Exception e) {
      throw new Exception("FirstName field is not displayed" + e);
    }
  }

  /**
   * To enterLastName_cc
   * 
   * @throws Exception
   * @param LastName :String
   */
  public void enterLastName_cc(String LastName) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find LastName text box"))
              .until(ExpectedConditions.visibilityOf(txtLastName));
      txtLastName.clear();
      txtLastName.sendKeys(LastName);
      Log.event("Entered the FirstName: " + LastName);
    } catch (Exception e) {
      throw new Exception("LastName field is not displayed" + e);
    }
  }

  /**
   * To enterMiddleName
   * 
   * @throws Exception
   * @param MiddleName :String
   */
  public void enterMiddleName(String MiddleName) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find MiddleName text box"))
              .until(ExpectedConditions.visibilityOf(txtMiddleName));
      txtMiddleName.clear();
      txtMiddleName.sendKeys(MiddleName);
      Log.event("Entered the MiddleName: " + MiddleName);
    } catch (Exception e) {
      throw new Exception("MiddleName field is not displayed" + e);
    }
  }

  /**
   * To selectType_CC
   * 
   * @throws Exception
   * @param type_cc :String
   */
  public void selectType_CC(String type_cc) throws Exception {
    try {
      System.out.println("Selecting Business Type");
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Type Selection field"))
              .until(ExpectedConditions.visibilityOf(selectType));
      Select typeCC = new Select(selectType);
      typeCC.selectByVisibleText(type_cc);
      System.out.println("Selected Business Type");
      Log.event("Selected type_cc: " + type_cc);
    } catch (Exception e) {
      throw new Exception("Error while selecting type_cc" + e);
    }
  }

  /**
   * To selectCountry_CC
   * 
   * @throws Exception
   * @param country_cc :String
   */
  public void selectCountry_CC(String country_cc) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Country Selection field"))
              .until(ExpectedConditions.visibilityOf(selectCountry));
      Select countryCC = new Select(selectCountry);
      countryCC.selectByVisibleText(country_cc);
      Log.event("Selected country_cc: " + country_cc);
    } catch (Exception e) {
      throw new Exception("Error while selecting country_cc" + e);
    }
  }

  /**
   * To selectTitle_CC
   * 
   * @throws Exception
   * @param selectTitle_CC :String
   */
  public void selectTitle_CC(String Title_cc) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find selectSuffix Selection field"))
              .until(ExpectedConditions.visibilityOf(selectTitle));
      Select TitleCC = new Select(selectTitle);
      TitleCC.selectByVisibleText(Title_cc);
      Log.event("Selected Title_cc: " + Title_cc);
    } catch (Exception e) {
      throw new Exception("Error while selecting Title_cc" + e);
    }
  }

  /**
   * To selectAddressList_CC
   * 
   * @throws Exception
   * 
   */
  public void selectAddressList_CC() throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find selectSuffix Selection field"))
              .until(ExpectedConditions.visibilityOf(AddressList));
      Select AddressListCC = new Select(AddressList);
      AddressListCC.selectByIndex(0);
      Log.event("Selected AddressList_cc: ");
    } catch (Exception e) {
      throw new Exception("Error while selecting AddressList_cc" + e);
    }
  }

  /**
   * To selectSuffix_CC
   * 
   * @throws Exception
   * @param Suffix_cc :String
   */
  public void selectSuffix_CC(String Suffix_cc) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find selectSuffix Selection field"))
              .until(ExpectedConditions.visibilityOf(selectSuffix));
      Select SuffixCC = new Select(selectSuffix);
      SuffixCC.selectByVisibleText(Suffix_cc);
      Log.event("Selected Suffix_cc: " + Suffix_cc);
    } catch (Exception e) {
      throw new Exception("Error while selecting Suffix_cc" + e);
    }
  }

  /**
   * To SelectOptMarketing
   * 
   * @throws Exception
   * 
   */
  public void SelectOptMarketing() throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find policy number text box"))
              .until(ExpectedConditions.invisibilityOfElementLocated(
                  By.cssSelector("div.spinning-on-load-bg-table-active")));
      selectRadioButton("fieldset#FS_QUE_05DBD1E701916C229357553>div.radio", "No");
      Log.event("Selected marketing preferences");
    } catch (Exception e) {
      throw new Exception("Policy Number field is not displayed" + e);
    }
  }

  /**
   * To SelectCommPreference_CCe
   * 
   * @throws Exception
   * 
   */
  public void SelectCommPreference_CC() throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find policy number text box"))
              .until(ExpectedConditions.invisibilityOfElementLocated(
                  By.cssSelector("div.spinning-on-load-bg-table-active")));
      selectRadioButton("fieldset#FS_QUE_05DBD1E701916C229357567>div.radio",
          "Correspondence by email");
      Log.event("Selected marketing preferences");
    } catch (Exception e) {
      throw new Exception("Policy Number field is not displayed" + e);
    }
  }

  /**
   * To clickfindAddress
   * 
   * @throws Exception
   * @param screenShot :boolean
   * 
   */
  public void clickfindAddress(boolean screenShot) throws Exception {
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Unable to find PostCode text box"))
            .until(ExpectedConditions.visibilityOf(btnfindAddress));
    try {
      btnfindAddress.click();

    } catch (Exception e) {
      if (!btnfindAddress.isDisplayed()) {
        throw new Exception("Find address Button is not visible" + e);
      }
    }
  }

  /**
   * To checkaddedInterestedParty
   * 
   * @throws Exception
   */
  public boolean checkaddedInterestedParty(String customername) throws Exception {
    WaitUtils.waitForSpinner(driver);
    WaitUtils.waitForelementToBeClickable(driver, addInterestedPartyBtn,
        "Waiting for add interested party button to be visible");
    // WaitUtils.waitForElementPresent(driver, addedParty, "Unable to find
    // added party");

    return GenericUtils.verifyWebElementTextEquals(addedParty, customername);
  }

  /**
   * To clickbacktoDashboard
   * 
   * @throws Exception
   * @param extentReport
   * @return CustDashboardPage
   * 
   */

  public CustDashboardPage clickbacktoDashboard(ExtentTest extentReport) throws Exception {
    try {
      lnkBacktoDashboard.click();
      WaitUtils.waitForSpinner(driver);

      Log.message("Clicked on BacktoDashboard Button", driver, extentReport);
      return new CustDashboardPage(driver, extentReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Click on lnkBacktoDashboard " + e);
    }

  }

  public CustDashboardPage clickbacktoDashboardonRnl(ExtentTest extentReport) throws Exception {
    try {
      lnkBacktoDashboardRnl.click();
      WaitUtils.waitForSpinner(driver);

      Log.message("Clicked on BacktoDashboard Button", driver, extentReport);
      return new CustDashboardPage(driver, extentReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Click on lnkBacktoDashboard " + e);
    }

  }

  /**
   * To verify the Take Payment Option
   * 
   * @throws Exception
   * @param extentReport
   * 
   */

  public void TakePayment(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, btnTakePayment,
          "unable to click take payment button");
      btnTakePayment.click();
      Log.message("Clicked on Take payment", extentReport);
      WaitUtils.waitForElementPresent(driver, paymentRef, "unable to find paymentRef");
      paymentRef.sendKeys("123456");
      Log.message("Entered Payment Ref", extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to Click on TakePayment button" + e);
    }

  }

  /**
   *
   * click on Add Policy holder button
   * 
   * @param extentReport
   * @throws Exception
   * 
   */
  public void clickAddPolicyHolderButton(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, btnAddPolicyHolder,
          "Unable to find Add Policy Holder button in new quote page");
      btnAddPolicyHolder.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Add Policy Holder button in New Quote page", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Add Policy Holder Button " + e);
    }
  }

  /**
   * To select Joint Policy Holder ContactType
   * 
   * @throws Exception
   * @param extentReport
   * @param ContactType :String
   */
  public void selectJointPolicyHolderContactType(String ContactType, ExtentTest extendReport)
      throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, selectJointPolicyContactType,
          "Unable to find Joint Policy Holder ContactType drop down");
      Select typeCC = new Select(selectJointPolicyContactType);
      typeCC.selectByVisibleText(ContactType);
      WaitUtils.waitForSpinner(driver);
      Log.message("Selected Contact Type : " + ContactType, extendReport);
    } catch (Exception e) {
      throw new Exception("Error while selecting Joint Policy Holder ContactType " + e);
    }
  }

  /**
   * To enter last name for joint policy
   * 
   * @param extendReport
   * @throws Exception
   * @param JointPolicy_lastname :String
   */

  public void enterJointPolicy_LastName(String JointPolicy_lastname, ExtentTest extendReport)
      throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, txtJointPolicyHolderLastName,
          "Unable to enter the last name");
      txtJointPolicyHolderLastName.clear();
      txtJointPolicyHolderLastName.sendKeys(JointPolicy_lastname);
      Log.message("Entered the Last Name : " + JointPolicy_lastname, extendReport);
    } catch (Exception e) {
      throw new Exception("Last Name is not entered " + e);
    }
  }

  /**
   * To enter first name for joint policy
   * 
   * @param extendReport
   * @throws Exception
   * @param JointPolicy_firstname :String
   */

  public void enterJointPolicy_FirstName(String JointPolicy_firstname, ExtentTest extendReport)
      throws Exception {
    try {

      txtJointPolicyHolderFirstName.clear();
      txtJointPolicyHolderFirstName.sendKeys(JointPolicy_firstname);
      Log.message("Entered the First Name : " + JointPolicy_firstname, extendReport);
    } catch (Exception e) {
      throw new Exception("First Name is not entered" + e);
    }
  }

  /**
   * To enter Date Of Birth for joint policy
   * 
   * @param extendReport
   * @throws Exception
   * @param JointPolicy_dateOfBirth :String
   */
  public void enterJointPolicy_DateOfBirth(String JointPolicy_dateOfBirth, ExtentTest extendReport)
      throws Exception {
    try {
      WebElement ele = driver.findElement(By.xpath(
          "//input[@id='C2__QUE_9233FAA81DFED7AF1007840'or @placeholder='DD/MM/YYYY' or type ='text']"));
      Actions builder = new Actions(driver);
      builder.moveToElement(ele).click().perform();


      ele.sendKeys(JointPolicy_dateOfBirth);

      // boolean flag = calJointPolicyHolderDob.isDisplayed();

      // System.out.println("the flag -----"+flag);


      WaitUtils.waitForSpinner(driver);
      Log.message("Entered the Date of Birth : " + JointPolicy_dateOfBirth, extendReport);
    }

    catch (Exception e) {
      throw new Exception("Date of Birth is not entered " + e);
    }
  }

  /**
   * To enter post code for joint policy
   * 
   * @param extendReport
   * @throws Exception
   * @param cn_postcode :String
   */
  public void enterJointPolicy_PostCode(String JointPolicy_postcode, ExtentTest extendReport)
      throws Exception {
    try {
      txtJointPolicyHolderPostCode.clear();
      txtJointPolicyHolderPostCode.sendKeys(JointPolicy_postcode);
      Log.message("Entered PostCode : " + JointPolicy_postcode, extendReport);
    } catch (Exception e) {
      throw new Exception("Post Code is not entered " + e);
    }
  }

  /**
   * To click Policy Holder Search Button in joint policy
   * 
   * @param extendReport
   * @throws Exception
   * 
   */
  public void clickPolicyHolderSearchButton(ExtentTest extendReport) throws Exception {

    try {
      WaitUtils.waitForelementToBeClickable(driver, btnJointPolicyHolderSearch,
          "Unable to find search button in add joint policy page");
      btnJointPolicyHolderSearch.click();
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, btnCreateNewJointPolicyholder);
      Log.message("Search button is clicked for joint policy holder", extendReport);
    } catch (Exception e) {
      throw new Exception("Unable to Click on search Button in joint policy holder page" + e);
    }
  }

  /**
   * To click select button in joint policy
   * 
   * @param extendReport
   * @throws Exception
   * td[id*='BUT_9233FAA81DFED7AF1371537_R1'] button[title*='Select']
   */
  public void clickJointPolicyHolderSelectButton(ExtentTest extendReport) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, btnJointPolicyHolderSelect,
          "Unable to find select button in add joint policy page");
      
      btnJointPolicyHolderSelect.click();
      
      Log.message("Select button is selected after joint policy search", extendReport);

    } catch (Exception e) {
      throw new Exception("Unable to Click on select Button in joint policy holder page" + e);
    }
  }

  /**
   *
   * click on search Policy holder button
   * 
   * @param extendReport
   * @throws Exception
   * @param testdata
   * 
   */
  public void searchJointPolicyHolder(HashMap<String, String> testdata, ExtentTest extendReport)
      throws Exception {
    try {
      selectJointPolicyHolderContactType(testdata.get("Contact_Type").toString(), extendReport);
      enterJointPolicy_LastName(testdata.get("Last Name").toString(), extendReport);
      enterJointPolicy_FirstName(testdata.get("First Name").toString(), extendReport);
      enterJointPolicy_DateOfBirth(testdata.get("Date of Birth").toString(), extendReport);
      enterJointPolicy_PostCode(testdata.get("Post Code").toString(), extendReport);
      clickPolicyHolderSearchButton(extendReport);
    } catch (Exception e) {
      throw new Exception("Unable to Add joint Policy Holder" + e);
    }
  }

  /**
   *
   * click on Add joint Policy holder button
   * 
   * @param extendReport
   * @throws Exception
   * @param testdata
   * 
   */
  public void addJointPolicyHolder(HashMap<String, String> testdata, ExtentTest extendReport)
      throws Exception {
    try {
      searchJointPolicyHolder(testdata, extendReport);
      clickJointPolicyHolderSelectButton(extendReport);
    } catch (Exception e) {
      throw new Exception("Unable to Add joint Policy Holder" + e);
    }
  }

  /**
   * click Save to create Quoted policy
   * 
   * @return CustDashboardPage
   * @param extendReport
   * @throws Exception : Custom Exception Message
   */
  public CustDashboardPage clicksave(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find save button on new quote page"))
              .until(ExpectedConditions.elementToBeClickable(btn_quoteSave));
      btn_quoteSave.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Save button", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on save Button" + e);
    }
    return new CustDashboardPage(driver, extentedReport).get();
  }

  /**
   *
   * verify Contact Header in policy holder table
   * 
   * @return boolean
   * @param extendReport
   * @throws Exception
   * 
   */
  public boolean verifyContactHeader(ExtentTest extendReport) throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, tablePolicyholder)) {
        GenericUtils.scrollIntoView(driver, headerContact);
        return GenericUtils.verifyWebElementTextEquals(headerContact, "Contact");
      } else {
        throw new Exception("Policy holder table does not exists");
      }

    } catch (Exception e) {
      throw new Exception("Error while veriying the Contact header in policy holder table" + e);
    }

  }

  /**
   *
   * verify Main Header in policy holder table
   * 
   * @return boolean
   * @param extendReport
   * @throws Exception
   * 
   */
  public boolean verifyMainHeader(ExtentTest extendReport) throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, tablePolicyholder)) {
        GenericUtils.scrollIntoView(driver, headerMain);
        return GenericUtils.verifyWebElementTextEquals(headerMain, "Main");
      } else {
        throw new Exception("Policy holder table does not exists");
      }

    } catch (Exception e) {
      throw new Exception("Error while veriying the Main header in policy holder table" + e);
    }

  }

  /**
   *
   * verify Header where bin icon is present in policy holder table
   *
   * @return boolean
   * @param extendReport
   * @throws Exception
   *
   */
  public boolean verifyBinIconHeader(ExtentTest extendReport) throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, tablePolicyholder)) {
        GenericUtils.scrollIntoView(driver, headerContact);
        return GenericUtils.verifyWebElementTextEquals(headerbinIcon, "");
      } else {
        throw new Exception("Policy holder table does not exists");
      }

    } catch (Exception e) {
      throw new Exception(
          "Error while veriying the Header where bin icon is present in policy holder table" + e);
    }

  }

  /**
   *
   * verify main contact in policy holder table
   * 
   * @return boolean
   * @param extendReport
   * @param NameToVerify
   * @param screebShot
   * @throws Exception
   * @param
   */
  public boolean verifyMainContact(String NameToVerify, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {
      if (WaitUtils.waitForElement(driver, tablePolicyholder)) {
        if (lstPolicyHeaderRows.size() == 0) {
          throw new Exception("Policy holder table does not conatain any row");
        }
        GenericUtils.scrollIntoView(driver, headerContact);
        String displayedMainContactName = lstPolicyHeaderRows.get(0)
            .findElement((By.cssSelector(firstNameOfPolicyHolderTable))).getText();

        if (displayedMainContactName.contains(NameToVerify) && lstPolicyHeaderRows.get(0)
            .findElement((By.cssSelector(firstCheckOfPolicyHolderTable))).isSelected()) {
          Log.message(NameToVerify + " : Name is displayed as main contact", driver, extentedReport,
              true);
          Log.message(
              "Main checkbox of Name ( " + NameToVerify + ") is selected in policy header table",
              driver, extentedReport, true);
          return true;
        } else {
          return false;
        }

      } else {
        throw new Error("Policy holder table is not present");
      }

    } catch (Exception e) {
      throw new Exception("Error while verifying main contact in policy holder table" + e);
    }

  }

  /**
   *
   * verify ContactName is present in policy holder table
   * 
   * @return boolean
   * @param extendReport
   * @throws Exception
   * @param screenShot
   * @param NameToVerify - PolicyHolder name to verify its availability
   */
  public boolean verifyPolicyHolderContactName(String NameToVerify, boolean screenShot,
      ExtentTest extentedReport) throws Exception {
    try {
      boolean boolStringFound = false;
      Integer totalRowSize = lstPolicyHeaderRows.size();
      GenericUtils.scrollIntoView(driver, headerContact);
      String cssContactName = "#C2__QUE_C6ED9305165D91DC2171257_R";
      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        String displayedMainContactName = lstPolicyHeaderRows.get(loopcount)
            .findElement((By.cssSelector(cssContactName + (loopcount + 1)))).getText();
        if (displayedMainContactName.contains(NameToVerify)) {
          Log.message(NameToVerify + " - Contact name is available in policy holder grid", driver,
              extentedReport, screenShot);
          boolStringFound = true;
          break;
        }
      }
      return boolStringFound;
    } catch (Exception e) {
      throw new Exception("Error while verifying contact name in policy holder table" + e);
    }
  }

  /**
   * verify bin icon present in policy holder table
   * 
   * @return boolean
   * @param extendReport
   * @throws Exception
   * @param screenShot
   * @param NameToVerify - Name of policy holder
   * 
   */
  public boolean verifyBinIcon(String NameToVerify, boolean screenShot, ExtentTest extentedReport)
      throws Exception {

    try {

      String cssContactName = "#C2__QUE_C6ED9305165D91DC2171257_R";

      boolean iconPresent = false;

      Integer totalRowSize = lstPolicyHeaderRows.size();
      if (totalRowSize == 0) {
        throw new Exception("No rows are available in policy holder grid");
      }
      GenericUtils.scrollIntoView(driver, headerContact);

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        WebElement row = lstPolicyHeaderRows.get(loopcount);

        String displayedContactName =
            row.findElement((By.cssSelector(cssContactName + (loopcount + 1)))).getText();

        if (displayedContactName.contains(NameToVerify)) {
          Log.message(NameToVerify + " - Contact name is available in policy holder grid", driver,
              extentedReport, screenShot);

          WebElement iconBin = row.findElement((By.cssSelector(cssBinIcon)));
          if (WaitUtils.waitForElement(driver, iconBin)) {
            iconPresent = true;
            break;
          }

        }

      }

      return iconPresent;

    } catch (Exception e) {
      throw new Exception("Error while verifying Bin Icon in policy holder table" + e);
    }
  }

  /**
   *
   * verify Main checkbox in policy holder table
   * 
   * @return boolean
   * @param extendReport
   * @throws Exception
   * @param screenShot
   * @param NameToVerify
   * 
   */
  public boolean verifyMaincheckbox(String NameToVerify, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {

      String cssContactName = "#C2__QUE_C6ED9305165D91DC2171257_R";
      String cssCheckBox = "#C2__QUE_65369060E37BB4AE3541860_0_R";
      boolean boolSelected = false;

      Integer totalRowSize = lstPolicyHeaderRows.size();
      if (totalRowSize == 0) {
        throw new Exception("No rows are available in policy holder grid");
      }
      GenericUtils.scrollIntoView(driver, headerContact);

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        WebElement row = lstPolicyHeaderRows.get(loopcount);

        String displayedContactName =
            row.findElement((By.cssSelector(cssContactName + (loopcount + 1)))).getText();

        if (displayedContactName.contains(NameToVerify)) {
          Log.message(NameToVerify + " - Contact name is available in policy holder grid", driver,
              extentedReport, screenShot);

          WebElement checkboxMain =
              row.findElement((By.cssSelector(cssCheckBox + (loopcount + 1))));
          if (checkboxMain.isSelected()) {
            boolSelected = true;
          }

        }

      }

      return boolSelected;
    } catch (Exception e) {
      throw new Exception("Error while verifying main checkbox in policy holder table" + e);
    }
  }

  /**
   * To click bin icon in policy holder table
   * 
   * @param screenShot
   * @param extendReport
   * @throws Exception
   * @param NameToclick - PolicyHolder name whose bin icon is to b clicked
   */
  public void clickBinIcon(String NameToclick, boolean screenShot, ExtentTest extentedReport)
      throws Exception {

    try {

      String cssBinIcon = "td[id*='C2__CELLp4_BUT_C6ED9305165D91DC2171263_R2'] button";

     /* WaitUtils.waitForElement(driver, binIcon);
      GenericUtils.scrollIntoView(driver, headerContact);

      Integer totalRowSize = lstPolicyHeaderRows.size();
      if (totalRowSize == 0) {
        throw new Exception("No rows are available in policy holder grid");
      }

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        WebElement row = lstPolicyHeaderRows.get(loopcount);

        String displayedContactName =
            row.findElement((By.cssSelector(cssContactName + (loopcount + 1)))).getText();
        if (displayedContactName.contains(NameToclick)) {
          Log.message(NameToclick + " - Contact name is available in policy holder grid", driver,
              extentedReport, screenShot);
*/
          WebElement iconBin = driver.findElement((By.cssSelector(cssBinIcon)));
          if (iconBin.isEnabled()) {
            iconBin.click();
            Log.message(NameToclick + " - Policyholder's Bin Icon is clicked successfully", driver,
                extentedReport, screenShot);
           
          }

        }

      
    catch (Exception e) {
      throw new Exception("Error while clicking bin icon" + e);
    }

  }

  /**
   *
   * To click Yes to remove policy
   * 
   * @param screenShot
   * @param extendReport
   * @throws Exception
   * @param NameToclick - Policyholder name to remove
   */
  public void clickYesToRemovePolicy(String NameToclick, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {
      clickBinIcon(NameToclick, screenShot, extentedReport);

      WaitUtils.waitForelementToBeClickable(driver, btnYesToRemovejointPolicy,
          "Failed to locate yes button to confirm remove joint policy");
      btnYesToRemovejointPolicy.click();
      Log.message("Yes button is clicked in the RemovePolicyholder window", extentedReport);
      WaitUtils.waitForinvisiblityofElement(driver, 120, cssBtnYesToRemovePolicyHolder,
          "Remove policy holder pop failed to close after waiting for 2 mins");

    } catch (Exception e) {
      throw new Exception("Error while clicking Yes button" + e);
    }

  }

  /**
   *
   * To click No to remove policy
   * 
   * @param screenShot
   * @param extendReport
   * @throws Exception
   * @param NameToclick - PolicyHolder name whose bin icon is to b clicked
   * 
   */
  public void clickNoToRemovePolicy(String NameToclick, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {
      clickBinIcon(NameToclick, screenShot, extentedReport);

      WaitUtils.waitForelementToBeClickable(driver, btnYesToRemovejointPolicy,
          "Failed to locate yes button to confirm remove joint policy");
      btnNoToRemovejointPolicy.click();
      Log.message("No button is clicked in the RemovePolicyholder window", extentedReport);
      WaitUtils.waitForinvisiblityofElement(driver, 120, cssBtnYesToRemovePolicyHolder,
          "Removepolicyholder pop up failed to close after waiting for 2 mins");

    } catch (Exception e) {
      throw new Exception("Error while clicking No button" + e);
    }

  }

  /**
   *
   * To click main checkbox in policy grid
   * 
   * @param screenShot
   * @param extendReport
   * @throws Exception
   * @param NameToclick - PolicyHolder name whose bin icon is to b clicked
   * 
   */
  public void clickMainCheckbox(String NameToclick, boolean screenShot, ExtentTest extentedReport)
      throws Exception {

    try {

      String cssContactName = "#C2__QUE_C6ED9305165D91DC2171257_R";
      String cssCheckBox = "#C2__QUE_65369060E37BB4AE3541860_0_R";

      Integer totalRowSize = lstPolicyHeaderRows.size();
      if (totalRowSize == 0) {
        throw new Exception("No rows are available in policy holder grid");
      }
      GenericUtils.scrollIntoView(driver, headerContact);

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        WebElement row = lstPolicyHeaderRows.get(loopcount);

        String displayedContactName =
            row.findElement((By.cssSelector(cssContactName + (loopcount + 1)))).getText();

        if (displayedContactName.contains(NameToclick)) {
          Log.message(NameToclick + " - Contact name is available in policy holder grid", driver,
              extentedReport);

          WebElement checkboxMain =
              row.findElement((By.cssSelector(cssCheckBox + (loopcount + 1))));
          if (!checkboxMain.isSelected()) {
            checkboxMain.click();
            Log.message("Main Checkbox is clicked for contact name - " + NameToclick, driver,
                extentedReport, screenShot);
            break;
          } else {
            Log.message("Main Checkbox is already clicked for contact name - " + NameToclick,
                driver, extentedReport, screenShot);
            break;
          }
        }

      }

    } catch (Exception e) {
      throw new Exception("Error while clicking the main checkbox" + e);
    }

  }

  /**
   *
   * To switch main policy holder in policy grid
   * 
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   * @param toSwitch - PolicyHolder name whose bin icon is to b clicked
   */
  public void switchMainPolicyholder(boolean toSwitch, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {
      if (toSwitch) {
        if (WaitUtils.waitForElement(driver, btnYesToChangeMainCheckbox)) {
          btnYesToChangeMainCheckbox.click();
          Log.message("Yes button is clicked to switch the main policy holder", driver,
              extentedReport, screenShot);
        } else {
          throw new Exception(
              "Yes button to switch the main policy holder is not available to click");
        }
      } else {
        if (WaitUtils.waitForElement(driver, btnNoToChangeMainCheckbox)) {
          btnNoToChangeMainCheckbox.click();
          Log.message("NO button is clicked to not switch the main policy holder", driver,
              extentedReport, screenShot);
        } else {
          throw new Exception(
              "No button to not switch the main policy holder is not available to click");

        }

      }
      WaitUtils.waitForinvisiblityofElement(driver, 180, cssBtnYesToswitchMainPolicyHolder,
          "Yes button did not close after for 3 minutes");

    } catch (Exception e) {
      throw new Exception("Error while clicking the main checkbox" + e);
    }

  }

  /**
   *
   * To verify joint policy search result
   * 
   * @param extentedReport
   * @throws Exception
   * @param nameToVerify - Policyholder name
   * 
   * @param addressToVerify - Address of policy holder
   * 
   * @param dob - DOB of policy holder
   * @return boolean
   * 
   */
  public boolean verifyJointPolicySearchResult(String nameToVerify, String addressToVerify,
      String dob, ExtentTest extentedReport) throws Exception {

    try {
      String cssJointPolicyName = "#C2__QUE_29E0D21D09DB6812743175_R";
      String cssJointPolicyAddress = "#C2__QUE_29E0D21D09DB6812743179_R";
      String cssJointPolciyDOB = "#C2__QUE_29E0D21D09DB6812743177_R";

      boolean boolStringFound = false;
      Integer totalRowSize = lstJointPolicySearchResult.size();

      if (totalRowSize == 0) {
        throw new Exception("Search result does not contain any result");
      }

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        String displayedName = lstJointPolicySearchResult.get(loopcount)
            .findElement((By.cssSelector(cssJointPolicyName + (loopcount + 1)))).getText();
        if (displayedName.contains(nameToVerify)) {
          Log.message(nameToVerify + " - Name is available in joint policy search result", driver,
              extentedReport);
          String displayedAddress = lstJointPolicySearchResult.get(loopcount)
              .findElement((By.cssSelector(cssJointPolicyAddress + (loopcount + 1)))).getText()
              .trim().replaceAll("\\s+", "");

          if (displayedAddress.contains(addressToVerify)) {
            Log.message(addressToVerify + " - Address is available in joint policy search result",
                driver, extentedReport);
            String displayedDob = lstJointPolicySearchResult.get(loopcount)
                .findElement((By.cssSelector(cssJointPolciyDOB + (loopcount + 1)))).getText();
            if (displayedDob.contains(dob)) {
              boolStringFound = true;
              break;
            }
          }
        }

      }

      return boolStringFound;

    } catch (Exception e) {
      throw new Exception("Error while verifying the joint policy search result" + e);
    }

  }

  /**
   *
   * To verify joint policy search result
   * 
   * @param nameToVerify - Policyholder name
   * 
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean verifyJointPolicySelectOption(String nameToVerify, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {
      String cssJointPolicyName = "#C2__QUE_29E0D21D09DB6812743175_R";
      String cssJointPolicyHolderSelect = "#C2__BUT_303A8E3DE0782D5E1156801_R";

      boolean boolSelectEnabled = false;
      Integer totalRowSize = lstJointPolicySearchResult.size();

      if (totalRowSize == 0) {
        throw new Exception("Search result does not contain any result");
      }

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        String displayedName = lstJointPolicySearchResult.get(loopcount)
            .findElement((By.cssSelector(cssJointPolicyName + (loopcount + 1)))).getText();
        if (displayedName.contains(nameToVerify)) {
          Log.message(nameToVerify + " - Name is available in joint policy search result", driver,
              extentedReport);

          WebElement selectOption = lstJointPolicySearchResult.get(loopcount)
              .findElement((By.cssSelector(cssJointPolicyHolderSelect + (loopcount + 1))));

          if (WaitUtils.waitForElement(driver, selectOption)) {
            Log.message("Select Option is enabled for the policyholder - " + nameToVerify, driver,
                extentedReport, screenShot);
            boolSelectEnabled = true;
            break;
          }

        }

      }

      return boolSelectEnabled;

    } catch (Exception e) {
      throw new Exception(
          "Error while verifying the select option of joint policy search result" + e);
    }

  }

  /**
   *
   * To verify joint policy search error message
   * 
   * @return boolean
   * @throws Exception
   * @param value - mandatory filed
   * 
   * @param errorToVerify - error message to verify
   * 
   */
  public boolean verifyErrorMessage(String errorToVerify) throws Exception {

    try {
      String textToVerify = errorToVerify.trim().replaceAll("\\s+", " ");

      return GenericUtils.verifyWebElementTextContains(errJointPolicyErrorMsg, textToVerify);

    } catch (Exception e) {
      throw new Exception("Error while verifying Error message while joint policy search" + e);
    }

  }

  /**
   *
   * To verify joint policy search result
   * 
   * @param extentedReport
   * @param takeScreenShot
   * @throws Exception
   * 
   */
  public void clickCreateNewJointPolicyHolder(ExtentTest extentedReport, boolean takeScreenShot)
      throws Exception {

    try {
      WaitUtils.waitForelementToBeClickable(driver, btnCreateNewJointPolicyholder,
          "Failed to locate Create New Joint policy holder button");
      btnCreateNewJointPolicyholder.click();
      Log.message("Successfully clicked CreateNewJointPolicyHolder button", driver, extentedReport,
          takeScreenShot);

    } catch (Exception e) {
      throw new Exception("Error while clicking the Create New Joint Policy Holder button" + e);
    }

  }

  /**
   *
   * To verify the Take Payment option
   * 
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void takePayment(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, but_takPayment, 60);
      WaitUtils.waitForelementToBeClickable(driver, but_takPayment, "Unable to Click the Take Payment Button"); 
      but_takPayment.click();
      Log.message("Clicked on Take Payment", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);

    } catch (Exception e) {
      throw new Exception("Unable to click on take payment Button, exception occured :" + e);
    }
    Thread.sleep(2000);
  }

  /**
   *
   * To select visa card in payment hub
   * 
   * @param extentedReport
   * @return CardDetailsPage
   * @throws Exception
   * 
   */

  public CardDetailsPage selectVisacard(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForPageLoad(driver);
      Thread.sleep(3000);
      driver.switchTo().frame(frame_sagepay);
      /*
       * WaitUtils.waitForElement(driver, lnkVisa); lnkVisa.click();
       */
      Log.message("Selected the Visa card", driver, extentedReport, true);
    } catch (Exception e) {
      throw new Exception("Error while selecting VISA Card" + e);
    }
    (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
        .withMessage("Spinner is active")).until(
            ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-image")));
    return new CardDetailsPage(driver, extentedReport).get();
  }


  /**
   *
   * To verify Payment Transaction
   * 
   * @param extentedReport
   * @return boolean
   * @throws Exception
   * 
   */

  public boolean verifyPaymentTrasaction(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, txt_paymntRef,
          "Failed to get payment reference id after payment");

      Log.message("Payment reference number :" + txt_paymntRef.getText(), extentedReport);
      return (Integer.parseInt(txt_paymntRef.getText()) > 0);

    } catch (Exception e) {
      throw new Exception("Trasaction was not successful, failed to fetch the transaction id " + e);
    }
  }

  /**
   * To create joint Policyholder
   * 
   * @throws Exception
   * @param testdata
   * @param extendReport
   * @param Screenshot :boolean
   */

  public void enterJointPolicyHolderDetails(HashMap<String, String> testdata, boolean screenShot,
      ExtentTest extendReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, cn_findAddress);
      Log.message("Entering Customer details");
      Thread.sleep(2000);

      selectTitle(testdata.get("Title").toString());
      Log.message("Title: " + testdata.get("Title").toString(), extendReport);
      Thread.sleep(2000);

      entercn_firstName(testdata.get("First Name").toString());
      Log.message("First Name: " + testdata.get("First Name").toString(), extendReport);
      Thread.sleep(2000);

      entercn_LastName(testdata.get("Last Name").toString());
      Log.message("Last Name: " + testdata.get("Last Name").toString(), extendReport);
      Thread.sleep(2000);

      entercn_DateOfBirth(testdata.get("Date of Birth").toString());
      Log.message("Date of Birth: " + testdata.get("Date of Birth").toString(), extendReport);
      Thread.sleep(2000);

      selectGender(testdata.get("Gender").toString());
      Log.message("Gender " + testdata.get("Gender").toString(), extendReport);
      Thread.sleep(2000);

      if (WaitUtils.waitForElement(driver, cn_findAddress))
        cn_findAddress.click();
      else
        throw new Exception("Find address button is not displayed");
      WaitUtils.waitForSpinner(driver);

      // selectAddress();
      Thread.sleep(2000);

      entercn_HomePhone(testdata.get("Home Phone").toString());
      Log.message("Home Phone: " + testdata.get("Home Phone").toString(), extendReport);
      Thread.sleep(2000);

      entercn_Email(testdata.get("Email").toString());
      Log.message("Email: " + testdata.get("Email").toString(), extendReport);
      Thread.sleep(2000);

      Log.message("Sucessfully entered the customer details", driver, extendReport, screenShot);

    } catch (Exception e) {
      try {
        if (!cn_firstName.isDisplayed()) {
          throw new Exception("Create Customer Page is not visible");
        }
      } catch (Exception f) {
        throw new Exception("Details are not entered properly" + f);
      }
      throw new Exception("Details are not entered" + e);
    }
  }

  /**
   *
   * To click view button in policy holder table
   * 
   * @param NameToclick
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   * 
   */
  public void clickViewButton(String NameToclick, boolean screenShot, ExtentTest extentedReport)
      throws Exception {

    try {

      String cssContactName = "#C2__QUE_C6ED9305165D91DC2171257_R";

      WaitUtils.waitForElement(driver, binIcon);
      GenericUtils.scrollIntoView(driver, headerContact);

      Integer totalRowSize = lstPolicyHeaderRows.size();
      if (totalRowSize == 0) {
        throw new Exception("No rows are available in policy holder grid");
      }

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        WebElement row = lstPolicyHeaderRows.get(loopcount);

        String displayedContactName =
            row.findElement((By.cssSelector(cssContactName + (loopcount + 1)))).getText();
        if (displayedContactName.contains(NameToclick)) {
          Log.message(NameToclick + " - Contact name is available in policy holder grid", driver,
              extentedReport, screenShot);

          WebElement viewButton = row.findElement((By.cssSelector(cssViewButton)));
          if (viewButton.isEnabled()) {
            viewButton.click();
            Log.message("View Button is clicked successfully", driver, extentedReport, screenShot);
            break;
          }

        }

      }

    } catch (Exception e) {
      throw new Exception("Error while clicking bin icon" + e);
    }

  }

  /**
   *
   * To Verify Joint Policy Details
   * 
   * @param testdata
   * @param screenShot
   * @param extentedReport
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean verifyJointPolicyDetail(HashMap<String, String> testdata, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {

      boolean ValueMatch = true;
      String title = testdata.get("Title").toString();
      String firstName = testdata.get("First Name").toString();
      String lastName = testdata.get("Last Name").toString();
      String dob = testdata.get("Date of Birth").toString();
      String postCode = testdata.get("Post Code").toString();
      String emailID = testdata.get("Email").toString();

      Thread.sleep(3000);
      WaitUtils.waitForelementToBeClickable(driver, cn_firstName,
          "Unable to locate the first name textbox");

      if (GenericUtils.verifyWebElementTextContains(selecttitle, title)) {
        Log.message(
            title + " - Given title is displayed in the title Textbox of joint policyholder",
            driver, extentedReport, screenShot);
      } else {
        ValueMatch = false;
      }

      if (verifyFirstNameTxtBoxValue(firstName)) {
        Log.message(
            firstName
                + " - Given name is displayed in the first name Textbox of joint policyholder",
            driver, extentedReport);
      } else {
        ValueMatch = false;
      }

      if (verifyLastNameTxtBoxValue(lastName)) {
        Log.message(
            lastName + " - Given name is displayed in the last name Textbox of joint policyholder",
            driver, extentedReport);
      } else {
        ValueMatch = false;
      }

      if (verifyDOBTxtBoxValue(dob)) {
        Log.message(dob + " - Given DOB is displayed in the dob Textbox of joint policyholder",
            driver, extentedReport);
      } else {
        ValueMatch = false;
      }

      if (cn_Email.getAttribute("value").contains(emailID)) {
        Log.message(
            emailID + " - Given emailID is displayed in the emailID Textbox of joint policyholder",
            driver, extentedReport);
      } else {
        ValueMatch = false;
      }

      if (verifyPostCodeTxtBoxValue(postCode)) {
        Log.message(
            postCode
                + " - Given Postcode is displayed in the Postcode textbox of joint policyholder",
            driver, extentedReport);
      } else {
        ValueMatch = false;
      }

      return ValueMatch;

    } catch (Exception e) {
      throw new Exception("Error while clicking bin icon" + e);
    }

  }

  /**
   *
   * To Verify Post code Text box Value
   * 
   * @param valueToVerify
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verifyPostCodeTxtBoxValue(String valueToVerify) throws Exception {
    try {
      return cn_postCode.getAttribute("value").trim().replaceAll("\\s+", "")
          .contains(valueToVerify);

    } catch (Exception e) {
      throw new Exception("Error while clicking bin icon" + e);
    }

  }

  /**
   *
   * To verify ContactName Alphabeticalorder
   * 
   * @param extentedReport
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verify_ContactSurName_Alphabeticalorder(ExtentTest extentedReport)
      throws Exception {
    try {

      List<String> myList = new ArrayList<String>();

      Integer totalRowSize = lstPolicyHeaderRows.size();
      if (totalRowSize == 0) {
        throw new Exception("The policy grid does not contain any contact name");
      }
      Log.message("The Policy grid has " + totalRowSize + " contacts", extentedReport);
      GenericUtils.scrollIntoView(driver, headerContact);
      String cssContactName = "#C2__QUE_C6ED9305165D91DC2171257_R";
      for (int loopcount = 1; loopcount < totalRowSize; loopcount++) {
        String displayedMainContactName = lstPolicyHeaderRows.get(loopcount)
            .findElement((By.cssSelector(cssContactName + (loopcount + 1)))).getText();
        String[] arrContactName = displayedMainContactName.split(" ");
        myList.add(arrContactName[2]);

      }

      if (!myList.isEmpty()) {
        Iterator<String> it = myList.iterator();
        String prev = it.next();
        while (it.hasNext()) {
          String next = it.next();
          if (prev.compareTo(next) > 0) {
            return false;
          }
          prev = next;
        }
      }
      return true;
    } catch (Exception e) {
      throw new Exception("Error while verifying the contact surname in alphabetical order " + e);
    }
  }

  public boolean lastName_Alphabeticalorder(ExtentTest extentedReport) throws Exception {
    try {

      List<String> myList = new ArrayList<String>();

      Integer totalRowSize = lstsearchpayor.size();
      if (totalRowSize == 0) {
        throw new Exception("The policy grid does not contain any contact name");
      }
      Log.message("The Policy grid has " + totalRowSize + " contacts", extentedReport);
      GenericUtils.scrollIntoView(driver, headerContact);
      String cssContactName = "#C2__C2__p4_QUE_CE93A4FCF8469E1D5823088_R";
      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        String displayedMainContactName = lstsearchpayor.get(loopcount)
            .findElement((By.cssSelector(cssContactName + (loopcount + 1)))).getText();
        String[] arrContactName = displayedMainContactName.split(" ");
        myList.add(arrContactName[2].substring(2));

      }

      if (!myList.isEmpty()) {
        Iterator<String> it = myList.iterator();
        String prev = it.next();
        while (it.hasNext()) {
          String next = it.next();
          if (prev.compareTo(next) > 0) {
            return false;
          }
          prev = next;
        }
      }
      return true;
    } catch (Exception e) {
      throw new Exception("Error while verifying the contact surname in alphabetical order " + e);
    }
  }

  public void createNewPayorDetails(ExtentTest extentReport) throws Exception

  {
    try {

      newPayorButton.click();
      Log.message("Clicked on New Payor Button", extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to Click on New payor button" + e);
    }

  }

  public boolean verify(WebDriver driver) {
    driver.switchTo().frame(frame_sagepay);
    WaitUtils.waitForElement(driver, paymentHub_title);

    boolean status = true;
    if (paymentHub_title.getText().equalsIgnoreCase("Secure Payment Page")) {
      Log.message("User is navigated to Payment Hub");
    }

    else {
      status = false;
      Log.message("User is not navigated to Payment Hub");
    }
    return status;

  }

  public void clickCrossIcon(ExtentTest extentedReport) throws Exception {
    try {
      crossButton.click();
      Log.message("Clicked on Cross Icon", extentedReport);
      WaitUtils.waitForSpinner(driver);

    } catch (Exception e) {
      throw new Exception("Unable to Click on Cross Icon : " + e);
    }
  }

  /**
   *
   * To click Save button
   * 
   * @param extentedReport
   * @throws Exception
   * @return CustDashboardPage
   * 
   */

  public CustDashboardPage clickSavedButton(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnSave);
      btnSave.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Save button is clicked in GetQuote page", extentedReport);
      return new CustDashboardPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact" + e);
    }

  }

  /**
   * Description :Verify the Interested party name on customer dashboard page
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @param CustName - Name to verify
   * 
   * @throws Exception : Custom Exception Message
   */

  public boolean verify_IntPartyName(String custName, ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      if (WaitUtils.waitForElement(driver, txtFld_IntPartyCustName, 1)) {
        Log.message("Intrested party displayed as : " + txtFld_IntPartyCustName.getText(), driver,
            extentedReport);
        return GenericUtils.verifyWebElementTextEquals(txtFld_IntPartyCustName, custName);
      } else
        return false;

    } catch (Exception e) {
      throw new Exception("Failed to fetch the interested party name : " + e);
    }

  }

  /**
   * click PaymentNext button
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */
  public void clickPaymentNext(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(btnPaymentNextone));
      btnPaymentNextone.click();
      Log.message("Clicked payment next button", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on View Button" + e);
    }
  }

  /**
   * Description :Verify whether the main policy holder checkbox is enabled
   * 
   * Return : boolean
   * 
   * @throws Exception : Custom Exception Message
   */

  public boolean isMainCheckboxEnabled() throws Exception {
    try {

      return firstMainCheckbox.isEnabled();
    } catch (Exception e) {
      throw new Exception("Failed to fetch the interested party name , throws exception : " + e);
    }

  }

  /**
   * Description :Verify whether mandatory terms&conditions message
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @param textToVerify - message to verify
   * 
   * @param screenShot - boolean
   * 
   * @throws Exception : Custom Exception Message
   */

  public boolean verifyMandatoryTandCMessage(String textToVerify, ExtentTest extentedReport,
      boolean screenShot) throws Exception {
    try {
      return GenericUtils.verifyWebElementTextContains(msgMandatoryTandC, textToVerify);

    } catch (Exception e) {
      throw new Exception("Failed to verify Mandatroy Terms&condition message : " + e);
    }

  }

  /**
   * Description :Verify whether mandatory terms&conditions is editable
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @param screenShot - boolean
   * 
   * @throws Exception : Custom Exception Message
   */

  public boolean verifyMandatoryTandCEditable(ExtentTest extentedReport, boolean screenShot)
      throws Exception {
    try {
      boolean isEditable = true;
      try {
        WaitUtils.waitForElement(driver, MandatoryTandC);
        MandatoryTandC.click();
        Log.message("Mandatory Terms&condition is clicked", driver, extentedReport, screenShot);
        rowMandatoryTandC.findElement((By.cssSelector(cssEditButton)));
      } catch (Exception ex) {
        if (ex.getMessage().contains("no such element")) {
          isEditable = false;
        }
      }

      return isEditable;

    } catch (Exception e) {
      throw new Exception("Erro while verifying mandatory Terms&condition are editable : " + e);
    }

  }

  /**
   * Description : To verify whether the star icon is available for newly added terms and condition
   * is added under terms & condition section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @param policyToVerify - Policy name
   * @param Screenshot
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean isStarIconDispalyed(String policyToVerify, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {

      String cssAddedPolicyRow = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_']";

      boolean StarAdded = false;
      List<WebElement> addedPolicyRow = driver.findElements(By.cssSelector(cssAddedPolicyRow));

      for (int i = 0; i < addedPolicyRow.size(); i++) {
        String displayedPolicyName = addedPolicyRow.get(i)
            .findElement(
                By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
            .getText();

        if (displayedPolicyName.equals(policyToVerify)) {

          Log.message(policyToVerify + " - T&C is available under terms & condition section",
              driver, extentedReport);

          if (addedPolicyRow.get(i)
              .findElement(
                  By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
              .isDisplayed()) {
            StarAdded = true;
            Log.message(
                "Star Icon is added for ( " + policyToVerify
                    + " ) T&C, which is available under terms & condition section",
                driver, extentedReport, Screenshot);
            break;
          }
        }
      }

      return StarAdded;

    } catch (Exception e) {
      throw new Exception("Unable to add T&C policy " + e);
    }
  }

  /**
   * Description : Select Terms & Conditions section
   * 
   * @param extentedReport
   * @param Screenshot
   * @param policyToSelect - Policy name
   * 
   * @throws Exception : Custom Exception Message
   */
  public void selectTandCPolicy(String policyToSelect, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {

      String cssPoilcyRow = "[id*='C2__C1__FMT_3A056143913CF4954126757_R']";

      String cssSelect = "id*='C2__C1__BUT_3A056143913CF4954126804_R";

      WaitUtils.waitForElement(driver, dropdownComponent);

      boolean policySelected = false;
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPoilcyRow));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        String displayedPolicyName = PolicyOptions.get(i)
            .findElement(By.cssSelector("[id*='C1__QUE_3A056143913CF4954126760_R" + (i + 1) + "']"))
            .getText();
        if (displayedPolicyName.equals(policyToSelect)) {

          WebElement rowToselect = PolicyOptions.get(i).findElement(
              By.cssSelector("[id*='C1__QUE_3A056143913CF4954126760_R" + (i + 1) + "']"));

          JavascriptExecutor executor = (JavascriptExecutor) driver;
          executor.executeScript("arguments[0].click();", rowToselect);

          policySelected = true;
          Log.message(policyToSelect + " - T&C is clicked", driver, extentedReport, Screenshot);
          WaitUtils.waitForElement(driver,
              driver.findElement(By.cssSelector("[" + cssSelect + (i + 1) + "']")));
          driver.findElement(By.cssSelector("[" + cssSelect + (i + 1) + "']")).click();

          break;
        }
      }

      if (!policySelected) {
        throw new Exception(policyToSelect + " - T&C is not available to select");
      }

      WaitUtils.waitForinvisiblityofElement(driver, 240, cssComponentDropDown,
          "Policy window did not close");
      Log.message(policyToSelect + " - T&C is selected", driver, extentedReport);

    } catch (Exception e) {
      throw new Exception("Unable to add T&C policy " + e);
    }
  }

  /**
   * Description : To verify whether the terms and condition is added under terms & condition
   * section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * @boolean Screenshot
   * @param policyToVerify - Policy name
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyTandCPolicyadded(String policyToVerify, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElement(driver, MandatoryTandC);
      String cssPolicy = "[id*='C1__QUE_304899FEFEF1DDEB353990_R1_']";
      boolean policyAdded = false;
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        String displayedPolicyName = PolicyOptions.get(i).getText();
        if (displayedPolicyName.equals(policyToVerify)) {
          policyAdded = true;
          Log.message(policyToVerify + " - T&C is available under terms & condition section",
              driver, extentedReport, Screenshot);
          break;
        }
      }

      return policyAdded;

    } catch (Exception e) {
      throw new Exception("Error while veriyfying whether the T&C is added" + e);
    }
  }

  /**
   * Description : To verify whether the terms and condition is editable under terms & condition
   * section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * @return screenshot
   * @param policyToClick - Policy name
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyTandC_Editable(String policyToClick, ExtentTest extentedReport,
      boolean Screenshot, String randomValue) throws Exception {
    try {

      boolean isEditable = false;
      boolean isClicked = false;
      WebElement rowToClick = null;
      Integer rowValue = null;

      WaitUtils.waitForElement(driver, MandatoryTandC);

      String cssPolicy = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_']";
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        rowToClick = PolicyOptions.get(i);
        String displayedPolicyName = rowToClick
            .findElement(
                By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
            .getText();
        if (displayedPolicyName.equals(policyToClick)) {
          rowToClick.click();
          Log.message(policyToClick + " - added T&C is clicked under terms&condition", driver,
              extentedReport, Screenshot);
          isClicked = true;
          rowValue = i;
          break;
        }
      }

      if (!isClicked) {
        throw new Exception(policyToClick + " is not availed to click");
      }

      if (WaitUtils.waitForElement(driver, rowToClick.findElement(
          By.cssSelector("[id*='C1__BUT_304899FEFEF1DDEB941705_R1_" + (rowValue + 1) + "']")))) {
        rowToClick
            .findElement(
                By.cssSelector("[id*='C1__BUT_304899FEFEF1DDEB941705_R1_" + (rowValue + 1) + "']"))
            .click();
        Log.message("Edit button is clicked to edit the T&C", driver, extentedReport);
      } else {
        throw new Exception("Edit button is not found to click");
      }

      String cssSaveButton = "[id*='C1__BUT_304899FEFEF1DDEB1181295_R1_" + (rowValue + 1) + "']";
      String cssTandCTextArea = "[id*='C1__QUE_A048EC24ED762AD77362602_R1_" + (rowValue + 1) + "']";

      if (WaitUtils.waitForElement(driver,
          rowToClick.findElement(By.cssSelector(cssTandCTextArea)))) {

        WaitUtils.waitForElement(driver, rowToClick.findElement(By.cssSelector(cssSaveButton)));
        WebElement txtAreaTandC = rowToClick.findElement(By.cssSelector(cssTandCTextArea));

        txtAreaTandC.click();

        // Edit the text
        ((JavascriptExecutor) driver).executeScript("arguments[0].innerText='" + randomValue + "'",
            txtAreaTandC);

        WaitUtils.waitForElement(driver, rowToClick.findElement(By.cssSelector(
            "[id*='C2__C1__p4_QUE_A048EC24ED762AD77362602_R1_" + (rowValue + 1) + "']")));
        WebElement newtxtAreaTandC = rowToClick.findElement(By.cssSelector(
            "[id*='C2__C1__p4_QUE_A048EC24ED762AD77362602_R1_" + (rowValue + 1) + "']"));

        String EditedText = newtxtAreaTandC.getText();
        if (EditedText.contains(randomValue)) {
          Log.message(policyToClick + " - Selected T&C contains edited string", driver,
              extentedReport, Screenshot);
          isEditable = true;
        }

      } else {
        throw new Exception("T&C text area is not found to edit");
      }

      return isEditable;

    } catch (Exception e) {
      throw new Exception("Error while clicking added T&C" + e);
    }
  }

  /**
   * Description : To verify the button for terms and condition is added under terms & condition
   * section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * @param Screenshot
   * @param policyToClick - Policy name
   * 
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyButtonForTandC(String policyToClick, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElement(driver, MandatoryTandC);

      boolean isButtonAvailable = false;
      WebElement rowToClick = null;

      String cssPolicy = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_']";
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        rowToClick = PolicyOptions.get(i);
        String displayedPolicyName = rowToClick
            .findElement(
                By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
            .getText();
        if (displayedPolicyName.equals(policyToClick)) {
          Log.message(policyToClick + " - added T&C is available under terms&condition", driver,
              extentedReport, Screenshot);
          rowToClick.click();
          break;
        }
      }

      if (WaitUtils.waitForElement(driver, rowToClick.findElement(By.cssSelector(cssEditButton)))) {
        isButtonAvailable = true;
        Log.message("Edit button is available for the added T&C", driver, extentedReport,
            Screenshot);
      }

      if (WaitUtils.waitForElement(driver,
          rowToClick.findElement(By.cssSelector(cssDeSelectButton)))) {
        isButtonAvailable = true;
        Log.message("De-Select button is available for the added T&C", driver, extentedReport,
            Screenshot);
      }

      return isButtonAvailable;

    } catch (Exception e) {
      throw new Exception("Error while clicking added T&C" + e);
    }
  }

  /**
   * click confirm payment
   *
   * @return CustDashboardPage
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   * 
   */
  public CustDashboardPage confirmPayment(ExtentTest extentedReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(btnConfirmPayment));
      btnConfirmPayment.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Confirm payment button", extentedReport);
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment" + e);
    }
  }

  /**
   * Click Change Payor button
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception : Custom Exception Message
   * 
   */
  public void clickChangePayorButton(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, btnChangePayor);
      WaitUtils.waitForelementToBeClickable(driver, btnChangePayor,
          "Change Payor button not enabled");
      btnChangePayor.click();
      WaitUtils.waitForElement(driver, fldPayorFirstName);
      Log.message("Clicked on Change Payor button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Change Payor button : " + e);
    }

  }

  /**
   * Click close Change Payor Modal
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception : Custom Exception Message
   * 
   */
  public void clickCloseChangePayorModal(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnCloseChangePayor);
      btnCloseChangePayor.click();
      Log.message("Clicked on Change Payor Close button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Change Payor Close button : " + e);
    }
  }

  /**
   * Click Cancel Change Payor Modal
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception : Custom Exception Message
   * 
   */
  public void clickCancelChangePayorModal(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnCancelChangePayor);
      btnCancelChangePayor.click();
      Thread.sleep(2000);
      Log.message("Clicked on Change Payor Cancel button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Change Payor Cancel button : " + e);
    }
  }

  /**
   * Verify Payor Modal is closed
   * 
   * @throws Exception
   * @return boolean
   */
  public boolean verifyPaymodalIsClosed() throws Exception {
    boolean isFound = false;
    if (!WaitUtils.waitForElement(driver, mdlChangePayorOpen))
      isFound = true;
    return isFound;
  }

  /**
   * Click Payor Search button
   * 
   * @param extentedReport
   * @param Screenshot
   * @throws Exception : Custom Exception Message
   * 
   */
  public void clickPayorSearchButton(ExtentTest extentedReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnPayorSearch);
      btnPayorSearch.click();
      Thread.sleep(3000);
      Log.message("Clicked on Payor Search button", driver, extentedReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Unable to click Payor Search button : " + e);
    }

  }

  /**
   * enter the change payor details
   * 
   * @return String
   * @param extentedReport
   * @param Screenshot
   * @param lastName
   * @param firstName
   * @param postcode
   * @throws Exception : Exception Message
   * 
   */
  public String selectChangePayor(String lastName, String firstName, String postcode,
      ExtentTest extentedReport, boolean Screenshot) throws Exception {
    String[] name = null;
    String payorName = null;

    try {
      searchAndSelectChangePayor(lastName, firstName, postcode, extentedReport, Screenshot);

      name = btnPayorGetName.getText().split("\\|");
      payorName = name[0].trim().replaceAll("\\s+", " ");
      Log.message("Selected the Payor : " + payorName, driver, extentedReport, Screenshot);

      btnPayorConfirm.click();
      Log.message("Clicked on Confirm Payor", driver, extentedReport);
      WaitUtils.waitForinvisiblityofElement(driver, 60, cssConfirmNewPayorButton,
          "Timed out waiting for 'Change Payor' popup to close");
    } catch (Exception e) {
      throw new Exception("Unable to click Change Payor button : " + e);
    }
    return payorName;
  }

  /**
   * verify the payor name
   * 
   * @return boolean
   * @param payorName
   * 
   */
  public boolean verifyPayorName(String payorName) {
    boolean status = false;
    if (fldPayor.getAttribute("value").replaceAll("\\s+", " ").contains(payorName)) {
      status = true;
    }
    return status;
  }

  /**
   * Verify the Payor fields error message
   * 
   * @param type - All Fields/PostCode/LastName
   * @param lastName
   * @param firstName
   * @param errorMsg
   * @param extentedReport
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean verifyPayorFieldsErrorMsg(String type, String lastName, String firstName,
      String postcode, String errorMsg, ExtentTest extentedReport) throws Exception {
    boolean status = false;
    if (type.equalsIgnoreCase("All Fields")) {
      clickPayorSearchButton(extentedReport, false);
      if (GenericUtils.verifyWebElementTextEquals(errorMsgPayorMandatoryFields,
          ERROR_MSG_PAYOR_ALLFIELDS))
        status = true;
      Thread.sleep(2000);
    } else if (type.equalsIgnoreCase("PostCode")) {
      fldPayorLastName.clear();
      fldPayorLastName.sendKeys(lastName);
      Log.message("Entered Payor Last Name : " + lastName, driver, extentedReport);
      fldPayorFirstName.clear();
      fldPayorFirstName.sendKeys(firstName);
      Log.message("Entered Payor First Name : " + firstName, driver, extentedReport);
      clickPayorSearchButton(extentedReport, false);
      if (GenericUtils.verifyWebElementTextEquals(errorMsgPayorMandatoryFields,
          ERROR_MSG_PAYOR_POSTCODE_OR_DOB))
        status = true;
      Thread.sleep(2000);
    } else if (type.equalsIgnoreCase("LastName")) {
      fldPayorLastName.clear();
      fldPayorFirstName.clear();
      fldPayorFirstName.sendKeys(firstName);
      Log.message("Entered Payor First Name : " + firstName, driver, extentedReport);
      fldPayorPostCode.clear();
      fldPayorPostCode.sendKeys(postcode);
      Log.message("Entered Payor PostCode : " + postcode, driver, extentedReport);
      clickPayorSearchButton(extentedReport, false);
      if (GenericUtils.verifyWebElementTextEquals(errorMsgPayorMandatoryFields,
          ERROR_MSG_PAYOR_LASTNAME))
        status = true;
      Thread.sleep(2000);
    }
    return status;
  }

  /**
   * Description : To verify edit and save terms&condition under terms & condition section
   * 
   * @param extentedReport
   * @param Screenshot
   * @param policyToEdit - Policy name
   * @return boolean
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyTandC_Edited(String policyToEdit, String randomValue,
      ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      boolean isedited = false;
      boolean isClicked = false;
      WebElement rowToClick = null;
      Integer rowValue = null;

      WaitUtils.waitForElement(driver, MandatoryTandC);

      String cssPolicy = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_']";
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        rowToClick = PolicyOptions.get(i);
        String displayedPolicyName = rowToClick
            .findElement(
                By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
            .getText();
        if (displayedPolicyName.equals(policyToEdit)) {
          rowToClick.click();
          Log.message(policyToEdit + " - added T&C is clicked under terms&condition", driver,
              extentedReport, Screenshot);
          isClicked = true;
          rowValue = i;
          break;
        }
      }

      if (!isClicked) {
        throw new Exception(policyToEdit + " is not availed to click");
      }

      String cssEditButton = "[id*='C1__BUT_304899FEFEF1DDEB941705_R1_" + (rowValue + 1) + "']";
      if (WaitUtils.waitForElement(driver, rowToClick.findElement(By.cssSelector(cssEditButton)))) {
        rowToClick.findElement(By.cssSelector(cssEditButton)).click();
        Log.message("Edit button is clicked to edit the T&C", driver, extentedReport);
      } else {
        throw new Exception("Edit button is not found to click");
      }

      String cssSaveButton = "[id*='C1__BUT_304899FEFEF1DDEB1181295_R1_" + (rowValue + 1) + "']";
      String cssTandCTextArea = "[id*='C1__QUE_A048EC24ED762AD77362602_R1_" + (rowValue + 1) + "']";

      if (WaitUtils.waitForElement(driver,
          rowToClick.findElement(By.cssSelector(cssTandCTextArea)))) {

        WaitUtils.waitForElement(driver, rowToClick.findElement(By.cssSelector(cssSaveButton)));
        WebElement txtAreaTandC = rowToClick.findElement(By.cssSelector(cssTandCTextArea));

        txtAreaTandC.click();

        // Edit the text
        ((JavascriptExecutor) driver).executeScript("arguments[0].innerText='" + randomValue + "'",
            txtAreaTandC);

        WebElement btnSave = rowToClick.findElement(By.cssSelector(cssSaveButton));
        btnSave.click();
        Log.message("Save button is clicked after editing the T&C", driver, extentedReport);

        WaitUtils.waitForinvisiblityofElement(driver, 180, cssSaveButton,
            "Save button did not close after waiting for 3 mins");

        cssPolicy = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_']";
        PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

        for (int i = 0; i < PolicyOptions.size(); i++) {
          rowToClick = PolicyOptions.get(i);
          String displayedPolicyName = rowToClick
              .findElement(
                  By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
              .getText();
          if (displayedPolicyName.equals(policyToEdit)) {
            rowToClick.click();
            Log.message(policyToEdit + " - added T&C is clicked under terms&condition", driver,
                extentedReport);
            isClicked = true;
            rowValue = i;
            break;
          }
        }

        if (!isClicked) {
          throw new Exception(policyToEdit + " is not availed to click to check edited T&C");
        }

        Log.message(policyToEdit + " - T&C is clicked again after values are edited", driver,
            extentedReport);

        String cssNewTxtArea =
            "[id*='C2__C1__QUE_A048EC24ED762AD77362602_R1_" + (rowValue + 1) + "']";
        txtAreaTandC = rowToClick.findElement(By.cssSelector(cssNewTxtArea));

        String EditedText = txtAreaTandC.getText();
        if (EditedText.contains(randomValue)) {
          isedited = true;
          Log.message("Edited strings are saved in T&C after clicking save button", driver,
              extentedReport, Screenshot);
        }

      } else {
        throw new Exception("T&C text area is not found to edit");
      }

      return isedited;

    } catch (Exception e) {
      throw new Exception("Error while saving the edited T&C" + e);
    }
  }

  /**
   * Selects the 'I have spoken to New Payor...' checkbox.
   * 
   * @param extentedReport
   * @throws Exception
   */
  public void tickSpokenDirectlyNewPayorCheckbox(ExtentTest extentedReport) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, chkSpokenDirectlyNewPayor);
      chkSpokenDirectlyNewPayor.click();
      Log.message("Checked the 'I have spoken to New Payor...' checkbox", extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Exception in checking the 'I have spoken to New Payor...' checkbox." + e);
    }
  }

  /**
   * Selects the 'Customer agrees...Direct Debit Guarantee' checkbox if it is not selected already.
   * 
   * @param extentedReport
   * @throws Exception
   */
  public void tickCustomerAgreesCheckbox(ExtentTest extentedReport) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, chkCustAgree);
      if (!chkCustAgree.isSelected()) {
        chkCustAgree.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Checked the 'Customer agrees...Direct Debit Guarantee' checkbox",
            extentedReport);
      } else {
        Log.message("The 'Customer agrees...Direct Debit Guarantee' checkbox is already checked",
            extentedReport);
      }
    } catch (Exception e) {
      throw new Exception(
          "Exception in checking the 'Customer agrees...Direct Debit Guarantee' checkbox." + e);
    }
  }

  public void clickAcceptbtn(ExtentTest extentedReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(btnAcceptPayment));
      btnAcceptPayment.click();
      Thread.sleep(2000);
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on 'Accept' payment button", extentedReport);

    } catch (Exception e) {
      throw new Exception("Exception in clicking accept button during monthly payment :" + e);
    }

  }

  /**
   * Description : To click cancel button for the Terms&Condition
   * 
   * @param extentedReport
   * @param Screenshot
   * @param policyToClick - Policy name
   * 
   * @throws Exception : Custom Exception Message
   */
  public void clickCancelBtnOfTandC(String policyToClick, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElement(driver, MandatoryTandC);

      boolean IsPresent = false;
      WebElement rowToClick = null;

      String cssPolicy = "[id*='C1__FMT_304899FEFEF1DDEB821953_R1_']";
      List<WebElement> PolicyOptions = driver.findElements(By.cssSelector(cssPolicy));

      for (int i = 0; i < PolicyOptions.size(); i++) {
        rowToClick = PolicyOptions.get(i);
        String displayedPolicyName = rowToClick
            .findElement(
                By.cssSelector("[id*='C1__QUE_304899FEFEF1DDEB353990_R1_" + (i + 1) + "']"))
            .getText();
        if (displayedPolicyName.equals(policyToClick)) {
          Log.message(policyToClick + " - added T&C is available under terms&condition", driver,
              extentedReport);
          IsPresent = true;
          break;
        }
      }

      if (!IsPresent) {
        throw new Exception(policyToClick + " - T&C is not available to click");
      }

      if (WaitUtils.waitForElement(driver,
          rowToClick.findElement(By.cssSelector(cssCancelButton)))) {
        rowToClick.findElement(By.cssSelector(cssCancelButton)).click();
        WaitUtils.waitForElement(driver, mdlConfirmationWnd);
        Log.message("Cancel button is clicked for the T&C - " + policyToClick, driver,
            extentedReport, Screenshot);
      } else {
        rowToClick.click();
        rowToClick.findElement(By.cssSelector(cssCancelButton)).click();
        WaitUtils.waitForElement(driver, mdlConfirmationWnd);
        Log.message("Cancel button is clicked for the T&C - " + policyToClick, driver,
            extentedReport, Screenshot);
      }

    } catch (Exception e) {
      throw new Exception("Error while clicking cancel button for T&C : " + e);
    }
  }

  /**
   * Description : To click NO button of confirmation dialog of Terms&condition
   * 
   * @param extentedReport
   * @param Screenshot
   * 
   * @throws Exception : Custom Exception Message
   */
  public void clickBtnOfTandCConfirmationWnd(String buttonName, ExtentTest extentedReport,
      boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElement(driver, mdlConfirmationWnd);
      if (buttonName.equals("yes")) {
        btnYesTandC.click();
        Log.message("Yes button is clicked to save the edit of T&C", driver, extentedReport,
            Screenshot);
      } else {
        btnNoTandC.click();
        Log.message("No button is clicked to not save the edit of T&C", driver, extentedReport,
            Screenshot);
      }
      WaitUtils.waitForinvisiblityofElement(driver, 180, cssTandCConfirmationdialog,
          "Warning dialog did not close after 3 min");

    } catch (Exception e) {
      throw new Exception("Error while clicking cancel button for T&C : " + e);
    }
  }

  /**
   * Description : To click NO button of confirmation dialog of Terms&condition
   * 
   * @param extentedReport
   * @param Screenshot
   * 
   * @throws Exception : Custom Exception Message
   */
  public void clickAllClearButton(ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnClearAll);
      btnClearAll.click();
      Thread.sleep(2000);
      Log.message("Clear All button is clicked", driver, extentedReport, Screenshot);

    } catch (Exception e) {
      throw new Exception("Error while clicking ClearAll button : " + e);
    }
  }

  /**
   * Description : To verify Search Field Cleared
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */
  public boolean verifySearchFieldCleared(ExtentTest extentedReport) throws Exception {
    try {

      Integer txtCleared = 0;
      boolean allCleared = false;

      if (fldPayorLastName.getText().equals("")) {
        txtCleared++;
        Log.message("Last Name text Field value is cleared", driver, extentedReport);
      }

      if (fldPayorFirstName.getText().equals("")) {
        txtCleared++;
        Log.message("First Name text Field value is cleared", driver, extentedReport);
      }

      if (fldPayorPostCode.getText().equals("")) {
        txtCleared++;
        Log.message("PostCode text Field value is cleared", driver, extentedReport);
      }

      if (txtCleared == 3) {
        allCleared = true;
      }

      return allCleared;
    } catch (Exception e) {
      throw new Exception("Error while clicking ClearAll button : " + e);
    }
  }

  /**
   * Search the change payor details
   * 
   * @return String
   * @param extentedReport
   * @param Screenshot
   * @param lastName
   * @param firstName
   * @param postcode
   * @throws Exception : Exception Message
   * 
   */
  public void clickSearchBtn_ChangePayor(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForElement(driver, btnPayorSearch);
      btnPayorSearch.click();
      Log.message("Search button is clicked for change payor search", driver, extentedReport);
      WaitUtils.waitForElement(driver, btnPayorSearchSelectRow1);
      // WaitUtils.waitForElementPresent(driver, btnPayorSearchSelectRow1,
      // "No search
      // results found");
    } catch (Exception e) {
      throw new Exception("Unable to click Change Payor button : " + e);
    }
  }

  /**
   * enter the change payor details
   * 
   * @param extentedReport
   * @param Screenshot
   * @param lastName
   * @param firstName
   * @param postcode
   * @throws Exception : Exception Message
   * 
   */
  public void enterChangePayorDetail(String lastName, String firstName, String postcode,
      ExtentTest extentedReport, boolean Screenshot) throws Exception {
    try {
      Thread.sleep(1000);
      WaitUtils.waitForelementToBeClickable(driver, fldPayorLastName,
          "Payor last name is not clickable");
      fldPayorLastName.clear();
      fldPayorLastName.sendKeys(lastName);
      Log.message("Entered Payor Last Name : " + lastName, driver, extentedReport);

      Thread.sleep(2000);
      fldPayorFirstName.clear();
      fldPayorFirstName.sendKeys(firstName);
      Log.message("Entered Payor First Name : " + firstName, driver, extentedReport);

      fldPayorPostCode.clear();
      fldPayorPostCode.sendKeys(postcode);
      Log.message("Entered Payor Postcode : " + postcode, driver, extentedReport, Screenshot);

    } catch (Exception e) {
      throw new Exception("Error while entering change payor detail : " + e);
    }
  }

  /**
   * Verify title of the page
   * 
   * @param screenshot :boolean
   * @param extentReport
   * @throws Exception
   * 
   */
  public boolean verifyTabTitles(String pagename, boolean Screenshot, ExtentTest extentReport)
      throws Exception {
    boolean status = false;
    WaitUtils.waitForElementPresent(driver, datacapturetitle,
        "Element is displayed on data capture page");
    switch (pagename.toLowerCase()) {

      case "datacapture":
        if (datacapturetitle.getAttribute("title").equals("Data Capture")) {
          status = true;
          return status;
        }
        break;

      case "pricepresentation":
        WaitUtils.waitForElementPresent(driver, pricePresentationtitle,
            "Element is displayed on data capture page");

        if (pricePresentationtitle.getAttribute("title").equals("Cover/Price Presentation")) {
          status = true;
          return status;
        }
        break;

      case "acceptance":
        WaitUtils.waitForElementPresent(driver, accptancetitle,
            "Element is displayed on data capture page");

        if (accptancetitle.getAttribute("title").equals("Acceptance")) {
          status = true;
          return status;
        }
        break;

    }
    return status;

  }

  /**
   * Verify existed data
   * 
   * @param extentReport
   *
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyExistedData(HashMap<String, String> testdata, String exp, boolean Screenshot,
      ExtentTest extentReport) throws Exception {
    boolean status = true;
    try {

      if ((testdata.get("YOB").toString()).equals(txtYearofBuild.getAttribute("value"))
          && !(coverStartDateonDC.getAttribute("value")).equals(exp)) {

        return status;
      } else {
        status = false;
        Log.message("New cover start date is displaying on policy data capture page"
            + coverStartDateonDC.getAttribute("value"));
        return status;
      }
    } catch (Exception e) {
      throw new Exception("Unable to Click on Next button : " + e);
    }
  }

  /**
   * Verify NB and RNL inception date
   * 
   * @param extentReport
   *
   * @throws Exception : Custom Exception Message
   */
  public boolean rnlIncpt(String incep, boolean Screenshot, ExtentTest extentReport)
      throws Exception {
    boolean status = true;
    try {
      if (!(coverStartDateonDC.getAttribute("value")).equals(incep)) {
        Log.message("New cover start date is displaying on policy data capture page"
            + coverStartDateonDC.getAttribute("value"));
        return status;
      } else {
        status = false;
        return status;
      }
    } catch (Exception e) {
      throw new Exception("Unable to Click on Next button : " + e);
    }

  }

  public CustDashboardPage clickbacktoDatacapture(ExtentTest extentReport) throws Exception {
    try {
      btnRnlBacktoDashboard.click();
      WaitUtils.waitForSpinner(driver);

      Log.message("Clicked on BacktoDatacapturepage", driver, extentReport);
      return new CustDashboardPage(driver, extentReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Click on btnRnlBacktoDashboard" + e);
    }

  }

  public void changePayorDetails(String lastName, String firstName, String postcode,
      ExtentTest extentedReport) throws Exception {
    try {
      fldPayorLastName.sendKeys(lastName);
      Log.message("Entered Payor Last Name : " + lastName, driver, extentedReport);
      fldPayorFirstName.sendKeys(firstName);
      Log.message("Entered Payor First Name : " + firstName, driver, extentedReport);
      fldPayorPostCode.sendKeys(postcode);
      Log.message("Entered Payor PostCode : " + postcode, driver, extentedReport);

    } catch (Exception e) {
      throw new Exception("Unable to enter values,throws exception : " + e);
    }

  }

  /**
   * Fills up Payment section input fields with the given test data and checks the 'Customer agrees
   * ...' checkbox.
   * 
   * @param testData
   * @param Screenshot
   * @param extentedReport
   * @throws Exception
   */

  public boolean selectPayment(String paymentPlan, HashMap<String, String> testData,
      boolean Screenshot, ExtentTest extentedReport) throws Exception {
    boolean status = false;
    try {
      if (paymentPlan.equals("Monthly")) {
        status = true;
        clickAddAccountDetails(extentedReport);
        checkBankAccount(testData.get("Account Number").toString(),
            testData.get("Sort Code").toString(), extentedReport);
        // enterAccountName(testData.get("Last Name").toString(),
        // extentedReport);
        saveAccountDetails(extentedReport);
        selectAccount(extentedReport);
        tickCustomerAgreesCheckbox(extentedReport);
        return status;
      } else
        return status;
    } catch (Exception e) {
      throw new Exception("Exception occured,Unable to Select Payment" + e);
    }
  }

  /**
   * Clicks on 'Select' button of first row in 'Add Account Details' section.
   * 
   * @param extentedReport
   */
  public void selectAccount(ExtentTest extentedReport) {
    WaitUtils.waitForElementPresent(driver, btnAcctSelect, "Select button not found");
    btnAcctSelect.click();
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked on select button of first account", extentedReport);
  }

  /**
   * Confirms the payment by clicking either 'Confirm' or 'Accept' button.
   * 
   * @param testdata
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   */

  public CustDashboardPage confirmPaymentRnl(String paymentplan, ExtentTest extentedReport)
      throws Exception {
    CustDashboardPage objCustDashboardPage = null;
    try {

      switch (paymentplan) {
        case "Annual":
          (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
              .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
              .withMessage("Unable to find Edit Details Link"))
                  .until(ExpectedConditions.elementToBeClickable(btnConfirmPayment));
          btnConfirmPayment.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on 'Confirm' payment button", extentedReport);
          break;
        case "Monthly":
          clickAcceptbtnRnl(extentedReport);
          break;
      }
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment. " + e);
    }
    return objCustDashboardPage;
  }

  public void clickAcceptbtnRnl(ExtentTest extentedReport) throws Exception {
    try {
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(rnlbtnAcceptPayment));
      rnlbtnAcceptPayment.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on 'Accept' payment button", extentedReport);

    } catch (Exception e) {
      throw new Exception("Exception in clicking accept button during monthly payment :" + e);
    }

  }

  /**
   * Description : To verify ChangePayor Error Message
   * 
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyChangePayorErrorMsg(String msg, ExtentTest extentedReport) throws Exception {
    try {
      return GenericUtils.verifyWebElementTextContains(errNoResultFound, msg);

    } catch (Exception e) {
      throw new Exception("Error while verifying ChangePayor Error Message : " + e);
    }
  }

  /**
   *
   * To verify joint policy search result
   * 
   * @param nameToVerify
   * @param screenShot
   * @param extentedReport
   * 
   */
  public boolean verifyChangePayorSearchResult(String nameToVerify, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {
      String cssChangePayorName = "#C2__C2__QUE_CE93A4FCF8469E1D5823088_R";
      String cssSelect = "#C2__C2__BUT_CE93A4FCF8469E1D5823094_R";

      boolean boolPresent = false;
      Integer totalRowSize = lstChangePayorSearchResult.size();

      if (totalRowSize == 0) {
        throw new Exception("Search result does not contain any result");
      }

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        String displayedName = lstChangePayorSearchResult.get(loopcount)
            .findElement((By.cssSelector(cssChangePayorName + (loopcount + 1)))).getText();

        if (displayedName.contains(nameToVerify)) {
          Log.message(nameToVerify + " - Name is available in change payor search result", driver,
              extentedReport);

          WebElement selectOption = lstChangePayorSearchResult.get(loopcount)
              .findElement((By.cssSelector(cssSelect + (loopcount + 1))));

          if (WaitUtils.waitForElement(driver, selectOption)) {
            boolPresent = true;
            selectOption.click();
            Log.message(nameToVerify + " - Given name is selected for change payor", driver,
                extentedReport, screenShot);
            break;
          }

        }

      }

      return boolPresent;

    } catch (Exception e) {
      throw new Exception("Error while verifying ChangePayor Search Result : " + e);
    }

  }

  /**
   * Search and select the change payor details
   * 
   * @param extentedReport
   * @param Screenshot
   * @param lastName
   * @param firstName
   * @param postcode
   * @throws Exception : Exception Message
   * 
   */
  public void searchAndSelectChangePayor(String lastName, String firstName, String postcode,
      ExtentTest extentedReport, boolean Screenshot) throws Exception {

    try {
      enterChangePayorDetail(lastName, firstName, postcode, extentedReport, Screenshot);
      clickSearchBtn_ChangePayor(extentedReport);
      // searchChangePayor(lastName, firstName, postcode, extentedReport,
      // Screenshot);
      WaitUtils.waitForElementPresent(driver, btnPayorSearchSelectRow1, "No search results found");

      btnPayorSearchSelectRow1.click();
      Log.message("Selected the first payor in search results", driver, extentedReport);
      WaitUtils.waitForElementPresent(driver, btnPayorConfirm,
          "Confirm New Payor button is not found");

    } catch (Exception e) {
      throw new Exception("Unable to search and select the payor : " + e);
    }
  }

  /**
   * Search and select the change payor details
   * 
   * @param extentedReport
   * @throws Exception : Exception Message
   * 
   */
  public void clickCancelbtn_ChangePayor(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForElement(driver, btnChangePayorCancel);
      btnChangePayorCancel.click();
      Log.message("Cancel button is clicked for change payor", driver, extentedReport);

      (new WebDriverWait(driver, 50).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Change payor window did not close after 50s"))
              .until(ExpectedConditions.invisibilityOf(btnChangePayorCancel));

    } catch (Exception e) {
      throw new Exception("Unable to click Cancel button for ChangePayor : " + e);
    }
  }

  /**
   *
   * To Verify first name Text box Value
   * 
   * @param valueToVerify
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verifyFirstNameTxtBoxValue(String firstName) throws Exception {
    try {
      return cn_firstName.getAttribute("value").contains(firstName);

    } catch (Exception e) {
      throw new Exception("Error while verifying firstName TextBox Value : " + e);
    }

  }

  /**
   *
   * To Verify first name Text box Value
   * 
   * @param valueToVerify
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verifyLastNameTxtBoxValue(String lastName) throws Exception {
    try {
      return cn_lastName.getAttribute("value").contains(lastName);

    } catch (Exception e) {
      throw new Exception("Error while verifying lastName TextBox Value : " + e);
    }

  }

  /**
   *
   * To Verify first name Text box Value
   * 
   * @param valueToVerify
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verifyDOBTxtBoxValue(String dob) throws Exception {
    try {
      return cn_dateofbirth.getAttribute("value").contains(dob);

    } catch (Exception e) {
      throw new Exception("Error while verifying DOB TextBox Value : " + e);
    }

  }

  /**
   * enter Change Payor DOB
   * 
   * @param extentedReport
   * @param dob
   * @throws Exception : Exception Message
   * 
   */
  public void enterChangePayorDOB(String dob, ExtentTest extentedReport) throws Exception {

    try {
      fldPayorDOB.clear();
      fldPayorDOB.sendKeys(dob);
      fldPayorDOB.click();
      Log.message("Entered Payor DOB : " + dob, driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable enter the change payor dob : " + e);
    }
  }

  /**
   * click Create New Payor button
   * 
   * @param extentedReport
   * @throws Exception : Exception Message
   */
  public void clickCreateNewPayor(ExtentTest extentedReport) throws Exception {

    try {
      btnCreateNewPayor.click();
      WaitUtils.waitForElement(driver, mdlCreateNewPayor);
      Log.message("Create new Payor button is clicked", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click CreateNewPayor button : " + e);
    }
  }

  /**
   * Selects the Customer Address.
   *
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void selectAddress(ExtentTest extentedReport) throws Exception {
    try {
      if (WaitUtils.waitForElement(driver, cn_AddressList, 2)) {
        (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
            .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
            .withMessage("Address not Listed"))
                .until(ExpectedConditions.visibilityOf(cn_AddressList));
        Select cn_Address = new Select(cn_AddressList);
        cn_Address.selectByIndex(0);
        Log.message("Selected the Address", driver, extentedReport);
      }
    } catch (Exception e) {
      throw new Exception("Unable to Select Address : " + e);
    }
  }

  /**
   * click Save Button for CreateNewPayor
   *
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void clickSaveBtn_CreateNewPayor(ExtentTest extentedReport) throws Exception {
    try {
      btnSaveCreateNewPayor.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Save button is clicked to save the created new payor", driver, extentedReport);

    } catch (Exception e) {
      throw new Exception("Unable to click save button in created new payor model : " + e);
    }
  }

  /**
   * enter the change payor details
   * 
   * @return String
   * @param extentedReport
   * @param Screenshot
   * @param lastName
   * @param firstName
   * @param postcode
   * @throws Exception : Exception Message
   * 
   */
  public String enterChangePayorDetails(String lastName, String firstName, String postcode,
      ExtentTest extentedReport, boolean Screenshot) throws Exception {
    String[] name = null;
    String payorName = null;

    try {
      System.out.println(lastName);
      System.out.println(firstName);

      fldPayorLastName.clear();
      fldPayorLastName.click();
      Thread.sleep(2000);
      fldPayorLastName.sendKeys(lastName);
      Thread.sleep(2000);
      Log.message("Entered Payor Last Name : " + lastName, driver, extentedReport);

      fldPayorFirstName.clear();
      fldPayorFirstName.sendKeys(firstName);
      Log.message("Entered Payor First Name : " + firstName, driver, extentedReport);

      fldPayorPostCode.clear();
      fldPayorPostCode.sendKeys(postcode);
      Log.message("Entered Payor Postcode : " + postcode, driver, extentedReport, Screenshot);
      clickPayorSearchButton(extentedReport, Screenshot);
      WaitUtils.waitForElementPresent(driver, btnPayorSearchSelectRow1, "No search results found");
      btnPayorSearchSelectRow1.click();
      Log.message("Selected the first payor in search results", driver, extentedReport);
      WaitUtils.waitForElementPresent(driver, btnPayorConfirm,
          "Confirm New Payor button is not found");
      name = btnPayorGetName.getText().split("\\|");
      payorName = name[0].trim();
      Log.message("Selected the Payor : " + payorName, driver, extentedReport, Screenshot);

      btnPayorConfirm.click();
      Log.message("Clicked on Confirm Payor", driver, extentedReport);
      WaitUtils.waitForinvisiblityofElement(driver, 60, cssConfirmNewPayorButton,
          "Timed out waiting for 'Change Payor' popup to close");
    } catch (Exception e) {
      throw new Exception("Unable to click Change Payor button : " + e);
    }
    return payorName;
  }

  /**
   * Description : To verify No CardDetail Message
   * 
   * @param msg
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyNoCardDetailMsg(String msg) throws Exception {
    try {
      return GenericUtils.verifyWebElementTextContains(msgNoCardDetail, msg);

    } catch (Exception e) {
      throw new Exception("Error while verifying NoCardDetail Message : " + e);
    }
  }

  /**
   * Description : To verify No CardDetail Message
   * 
   * @param msg
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyNoAccountDetailMsg(String msg) throws Exception {
    try {
      return GenericUtils.verifyWebElementTextContains(msgNoAccountDetail, msg);

    } catch (Exception e) {
      throw new Exception("Error while verifying NoAccountDetail Message : " + e);
    }
  }

  /**
   * Description : To verify No CardDetail Message
   * 
   * @param msg
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyAccountNameDisplayed(String accName) throws Exception {
    try {

      String cssAccName = "#C2__p4_QUE_EE2C7D9B8CC571FB2770389_R";

      boolean boolStringFound = false;
      Integer totalRowSize = lstAccountDetail.size();

      if (totalRowSize == 0) {
        throw new Exception("Account detail is not displayed");
      }

      for (int loopcount = 0; loopcount < totalRowSize; loopcount++) {
        String displayedName = lstAccountDetail.get(loopcount)
            .findElement((By.cssSelector(cssAccName + (loopcount + 1)))).getText();
        if (displayedName.contains(accName)) {
          boolStringFound = true;
        }
      }

      return boolStringFound;

    } catch (Exception e) {
      throw new Exception("Error while verifying Account name : " + e);
    }
  }

}// NewquotePage
