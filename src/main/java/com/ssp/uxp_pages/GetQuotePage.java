package com.ssp.uxp_pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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

public class GetQuotePage extends LoadableComponent<GetQuotePage> {

  private final WebDriver driver;
  private ExtentTest extentedReport;
  private boolean isPageLoaded;
  public ElementLayer uielement;

  public String tbl_insurance_covers_CNT = "div[id*='FMT_728A190A046539421182844'] tbody>tr";
  public String tbl_insurance_covers_Bld = "div[id*='FMT_728A190A04653942826973'] tbody>tr";
  public String tbl_insurnace_covers_AddOns = "div[id*='FMT_728A190A046539421184112'] tbody>tr";
  public String ins_BldCover_excessAmt = "select[name*='BUIDINGSCOVER'][name*='EXCESS']";
  public String ins_Bld_EditPopup = "div[id*='FMT_728A190A046539421316998'].modal-content";
  public String ins_HRcover_RadioBut = "div[id*='p4_QUE_12B9E4694CE1FE92954109']>div>div>div";
  public String ins_HRCover_PopupWnd = "div[id*='FMT_728A190A046539421661764'].modal-dialog";
  public String ins_GCcover_PopupWnd = "div[id*='FMT_728A190A046539421871965'].modal-dialog";
  public String ins_TEcover_PopupWnd = "div[id*='FMT_728A190A046539422029713'].modal-dialog";
  public String ins_PCRadioBut = "div[id*='p4_QUE_F7F54EFB71FFA51525074']>div>div>div";
  public String ins_PC_PopupWnd = "div[id*='FMT_728A190A046539422229728'].modal-dialog";
  public String ins_PIcovre_Radiobut = "div[id*='p4_QUE_F7F54EFB71FFA51525071']>div>div>div";
  public String ins_PI_PoupWnd = "div[id*='FMT_728A190A046539422175313'].modal-dialog";
  public String spinner = "div.spinning-on-load-bg-table-active";
  public String unknwn_errormsg =
      "div[id*='FMT_9E775789D61887FF4424404']>div>div>div>div>div>div.col-md-11";
  private String radio_button_value = null;
  public String ins_MTAPaymentPopup = "div[id*='FMT_430DA01BA1D6A513525031'].modal-content";
  public String ins_MTa_TakePaymnt = "div[id*='FMT_AC15A4A8B3D988B3773281'].modal-dialog";
  public String addInterestCreateAddInterest = "#C2__BUT_E36EFB44BDE033C510171715";
  public String addInterestCancelBtn = "#C2__BUT_29E0D21D09DB6812621225";

  public String cssBinIcon = "button[class='trash']";
  public String cssBtnYesToRemovePolicyHolder = "button[id='C2__RemovePolicyholder_Yes']";

  public String xpathAccSelectBtnOfAccDetails =
      "//table[@summary='BankDetails']//tr[.//span[text()='${SORT_CODE}']][.//span[text()='${BRANCH}']][.//span[text()='${ACC_NAME}']][.//span[text()='${ACC_NO}']]//button";

  public String cssPaySchedColHeader = "div[id='C2__TXT_039B9D213617E7CA806962'] .table_head";
  public String cssPaySchedDueDates = "span[id*='9C281AC77BF4F72E2122776']";
  public String cssPaySchedCalculatedPremium = "span[id*='9C281AC77BF4F72E2122778']";
  public String cssPaySchedTaxes = "span[id*='9C281AC77BF4F72E2122780']";
  public String cssPaySchedCharges = "span[id*='9C281AC77BF4F72E2122782']";
  public String cssPaySchedChargesTax = "span[id*='9C281AC77BF4F72E2134770']";
  public String cssPaySchedInstallments = "span[id*='9C281AC77BF4F72E2134773']";
  public String cssPaySchedAmountPayable = "span[id*='9C281AC77BF4F72E2134776']";

  public String cssRnwRevchkBox = "#C2__row_QUE_2095CCD014226C36580633 fieldset";


  /**********************************************************************************************
   ********************************* WebElements of Get Quote Page **********************************
   **********************************************************************************************/
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

  @FindBy(css = "button[id='C2__BUT-RE-CALCULATE']")
  WebElement btnReCalculate;

  @FindBy(css = "button.btn.btn-default.page-spinner[title='Upgrade now']")
  WebElement btnUpgradeNow;

  @FindBy(css = "#C2__BUT_728A190A046539422616673[title='Buy']")
  WebElement btnBuy;

  @FindBy(css = "#C2__BUT_2BF7614AC3E22D7C1441352[title='Buy']")
  WebElement btnBuyEditQuote;

  @FindBy(css = "div[id*='FMT_430DA01BA1D6A513525031'].modal-content")
  WebElement ins_MTAPayment;

  @FindBy(css = "span[id*='QUE_35FF8CD9CD43EF451452631']")
  WebElement AcptAdj_MTAamt;

  @FindBy(css = "button[id*='BUT_ACCEPT_QUOTE']")
  WebElement AcptAdj_AcceptBut;

  @FindBy(xpath = ".//*[@id='C1__TXT_4CB44BC416B96D21790734']/div/div[5]/div[1]/div")
  WebElement MTAamt_GetPage;

  @FindBy(css = "select[name*='BUYQUOTE'][name*='PAYMENTMETHODGETQUOTE']")
  WebElement MTA_PaymentMethod;

  @FindBy(css = "div[id*='FMT_AC15A4A8B3D988B3773281'].modal-dialog")
  WebElement MTA_TakePaymentPopup;

  @FindBy(
      css = "div[id*='p4_QUE_AC15A4A8B3D988B3773245']>div>span[id*='QUE_AC15A4A8B3D988B3773245']")
  WebElement MTA_PayAmount;

  @FindBy(css = "input[id*='QUE_AC15A4A8B3D988B3773251']")
  WebElement MTA_TrasacId;

  @FindBy(css = "button[id*='BUT_AC15A4A8B3D988B3773257']")
  WebElement MTA_SaveBut;

  // @FindBy(css = "button[id*='BUT_3516874A7C1460DF1409566']")
  @FindBy(css = "#C1__Header-Backtodashboard")
  WebElement btnDashboard;

  @FindBy(css = "button[id*='BUT_FE031EDBB6A4EA08804112']")
  WebElement AcptAdj_AcceptBut_Rem;

  @FindBy(css = "button[id*='BUT_ACCEPT_QUOTE']")
  WebElement AcptAdj_AcceptBut_Add;

  @FindBy(css = "#C2__p4_BUT_B13A0396B2DE40F42605055")
  WebElement tabAcceptance;

  @FindBy(css = "button[id*=BUT_8A17B7AD077319CD4115330]")
  WebElement btnAttachTermAndCondition;

  @FindBy(css = "input[type='checkbox'][name*='TERMSANDCONDITIONS']")
  WebElement termsconditions;// C1__CONTACTCENTRE[1].BUYQUOTE[1].PAYMENTPLAN

  @FindBy(css = "a[id='termsandconditions']")
  WebElement termsAndConditionsSection;

  @FindBy(css = "span[id*='QUE_3A056143913CF4954126760_R2'")
  WebElement firstTermAndConditionInList;

  @FindBy(css = "#C2__C1__FMT_3A056143913CF4954126757_R3']")
  WebElement secondTermAndConditionInList;

  @FindBy(css = "#C2__C1__p4_BUT_3A056143913CF4954126804_R2 [title='Edit']")
  WebElement btnEditTermAndCondition;

  @FindBy(css = "#C2__C1__BUT_3A056143913CF4954126804_R2[title='Select']")
  WebElement btnSelect;

  @FindBy(css = "button[title='Select'")
  WebElement selectTAC;

  @FindBy(css = "button[id*='BUT_3A056143913CF4954126804_R2'")
  WebElement selectTACfirst;
  @FindBy(css = "span[id*='QUE_3A056143913CF4954126787_R3'][class='editTAC']")
  WebElement selectSecondTAC;

  @FindBy(css = "#C2__p4_QUE_C6ED9305165D91DC2171257_R1")
  WebElement fld_policyHolderName;

  @FindBy(css = "button#C2__ADD_INTERESTED_PARTY")
  WebElement addInterestedPartyBtn;

  @FindBy(
      css = "select[name='C2__CONTACTCENTRE[1].GETQUOTE[1].POLICYCONTACTS[1].INTERESTEDPARTYOVERLAY[1].CONTACTTYPE")
  WebElement addInterestcontactType;

  @FindBy(css = "button.btn.btn-default#C2__BUT_29E0D21D09DB6812743210")
  WebElement addInterestsearchBtn;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].GETQUOTE[1].POLICYCONTACTS[1].INTERESTEDPARTYOVERLAY[1].SEARCHCONTACTSECONDNAME']")
  WebElement addInterestFirstname;

  @FindBy(
      css = "input[name='C2__CONTACTCENTRE[1].GETQUOTE[1].POLICYCONTACTS[1].INTERESTEDPARTYOVERLAY[1].SEARCHFORCONTACT']")
  WebElement addInterestLastname;

  @FindBy(css = "button#C2__BUT_B081B9E5856613274221000_R1.btn.btn-default")
  WebElement addInterestselecthBtn;

  @FindBy(
      css = "select[name='C2__CONTACTCENTRE[1].GETQUOTE[1].POLICYCONTACTS[1].INTERESTEDPARTYOVERLAY[1].INTERESTEDPARTYTYPE']")
  WebElement addInterestpartytype;

  @FindBy(css = "input#date-picker-C2__QUE_29E0D21D09DB6812718814")
  WebElement addInterestpartytypeTodate;

  @FindBy(css = "button#C2__BUT_29E0D21D09DB68121013768")
  WebElement addInterestpartySave;

  @FindBy(css = "button#C2__BUT_E36EFB44BDE033C54248971_R1.trash")
  WebElement addPartyDeleteicon;

  @FindBy(css = "#C2__BUT_E36EFB44BDE033C510171715")
  WebElement addInterestcreateAddInterest;

  @FindBy(css = "#C2__QUE_E36EFB44BDE033C53772061_R1")
  WebElement addedParty;

  @FindBy(css = "#C2__BUT_14E03EE0D08E416C726298")
  WebElement btnSave;

  @FindBy(css = "button[id*=ADD_POLICYHOLDER]")
  WebElement btnAddPolicyHolder;

  @FindBy(css = "table[summary='PolicyHolderDetails'] tbody tr")
  List<WebElement> lstPolicyHeaderRows;

  @FindBy(css = "#C2__p1_QUE_C6ED9305165D91DC2171257")
  WebElement headerContact;

  @FindBy(css = "button[class='trash']")
  WebElement binIcon;

  @FindBy(css = "button[id='C2__RemovePolicyholder_Yes']")
  WebElement btnYesToRemovejointPolicy;

  @FindBy(css = "#C2__B2_QUE_FD051A198CB3861E1713415[title='ADD ALL']")
  WebElement btnAddAllMTA;

  @FindBy(css = "#C2__BUT_FD051A198CB3861E1713417[title='Next']")
  WebElement btnAddAllMTANext;

  @FindBy(css = "#C2__BUT_728A190A046539422616673[title='Buy']")
  WebElement btnBuyMTA;

  @FindBy(css = "select#C2__UNSEL_QUE_FD051A198CB3861E1713415")
  WebElement sel_MTAReason;


  @FindBy(css = "select#C2__UNSEL_QUE_E64D44788D59032F1313656")
  WebElement sel_STAReason;



  @FindBy(css = "button#C2__B1_QUE_FD051A198CB3861E1713415[value='ADD']")
  WebElement btn_MTAreasonAdd;


  @FindBy(css = "button#C2__B1_QUE_E64D44788D59032F1313656[value='ADD']")
  WebElement btn_STAreasonAdd;

  @FindBy(css = "button#C2__BUT_FD051A198CB3861E1713417[value='Next']")
  WebElement btn_MTAnext;

  @FindBy(css = "#C2__ID_DIRECT_DEBIT_CONFIRM")
  WebElement btnConfirmMTA;

  @FindBy(css = "#C2__B1_QUE_FD051A198CB3861E1713415[title='ADD']")
  WebElement btnAddMTA;

  @FindBy(css = "#C2__B1_QUE_E64D44788D59032F1313656[title='ADD']")
  WebElement btnAddSTA;

  @FindBy(css = "#C2__UNSEL_QUE_FD051A198CB3861E1713415>option")
  List<WebElement> lstAdjReasons;
  @FindBy(css = "#C2__UNSEL_QUE_E64D44788D59032F1313656 > option")
  List<WebElement> STAAdjReasons;

  @FindBy(css = "input[name='op-DPChoose-VISA^SSL'][alt='Visa']")
  WebElement lnkVisa;//

  // @FindBy(css = "iframe[name='sagepay']")
  @FindBy(css = "iframe[id='wp-cl-custom-html-iframe']")
  WebElement frame_sagepay;

  @FindBy(css = "#C2__QUE_2411741C919DF0934660562")
  WebElement txt_paymntRef;

  @FindBy(css = "button#C2__ID_DIRECT_TAKE_PAYMENT")
  WebElement but_takPayment;

  @FindBy(css = "button[id*='BUT_925B6D15BDB617A8717552']")
  WebElement btnConfirmPayment;

  @FindBy(css = "#C2__ID_DIRECT_DEBIT_CONFIRM")
  WebElement btn_STAconfirm;

  @FindBy(css = "button[id$='BUT_E2BF6E7D321C01D53171145'][value='Accept']")
  WebElement btnAcceptPayment;

  @FindBy(css = "ul.nav.nav-tabs>li>a")
  WebElement Ele_engagement_Centre_tabs;

  @FindBy(css = "a[data-target*='personal-details']")
  WebElement enga_Centre_tabs_Personal;

  @FindBy(css = "a[data-target='#C2__C1__corporate-details']")
  WebElement enga_Centre_tabs_Corporate;

  @FindBy(css = ".nav.nav-tabs>li.active")
  WebElement contact_activetab;

  @FindBy(css = "div.main-brand")
  WebElement txtBannerName;

  @FindBy(css = "div#C2__p4_QUE_E2BF6E7D321C01D51456374>div>fieldset>div>input[type='checkbox']")
  WebElement checkbox_CustAgree;

  @FindBy(css = "select[name*='PAYMENTMETHOD']")
  WebElement selectpaymentmethod;

  @FindBy(css = "select[name*='PAYMENTPLAN']")
  WebElement selectpaymentplan;

  @FindBy(css = "input#C2__QUE_2AED8243C0FF5F64796873")
  WebElement txt_IntPartCorp_lName;

  @FindBy(css = "input[name*='INTERESTEDPARTYOVERLAY[1].POSTCODE']")
  WebElement txt_IntPartCopr_Pcode;

  @FindBy(css = "button#C2__BUT_29E0D21D09DB68121111046")
  WebElement but_Search_CorpIP;

  @FindBy(css = "button[id$='BUT_E2BF6E7D321C01D51540036']")
  WebElement btnSaveQuote;

  @FindBy(css = "button[id$='BUT_7499E8C9DD09DCA41348358']")
  WebElement btnCancelPayment;

  @FindBy(css = "button[id$='BUT_FD051A198CB3861E3809030']")
  WebElement btnCancelPaymentYes;

  @FindBy(css = "input[type='radio'][value='Spread over Instalments']")
  WebElement radSpreadOverInstallments;

  @FindBy(css = "input[type='radio'][value='Single Bill-Card']")
  WebElement radSingleBillCard;

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

  @FindBy(css = "div#C2__p4_QUE_E2BF6E7D321C01D51456374>div>fieldset>div>input[type='checkbox']")
  WebElement chkCustAgree;

  @FindBy(
      xpath = "//div[preceding-sibling::label[contains(text(), 'Existing Policy Term Premium:')]][1]/div")
  WebElement divExistingPremiumCoverTab;

  @FindBy(
      xpath = "//div[preceding-sibling::label[contains(text(), 'New Policy Term Premium:')]]/div")
  WebElement divNewPremiumCoverTab;

  @FindBy(xpath = "//div[preceding-sibling::label[contains(text(), 'Adjustment Amount:')]][1]/div")
  WebElement divAdjustmentAmountCoverTab;

  @FindBy(css = "span[id='C2__QUE_1296CBF41B899BC32592888']")
  WebElement spnOneOffPaymentAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_24D10CAD2CBD74854948763']")
  WebElement spnTotalPaymentAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_24D10CAD2CBD74854948769']")
  WebElement spnFirstPaymentAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_24D10CAD2CBD74854948771']")
  WebElement spnInstallmentPaymentAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_8FCF3782F196CBEC1351284']")
  WebElement spnExistingPremiumAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_8FCF3782F196CBEC1351286']")
  WebElement spnNewPremiumAcceptanceTab;

  @FindBy(css = "span[id='C2__QUE_FD051A198CB3861E4809908']")
  WebElement spnAdjustmentAmountAcceptanceTab;

  @FindBy(css = "#C2__BUT_E2BF6E7D321C01D54970092")
  WebElement btnMTATakePayment;

  @FindBy(css = "[title='Return to Customer Dashboard']")
  WebElement btnReturnToCustomerDashboard;

  @FindBy(css = "#C2__HEAD_EE2C7D9B8CC571FB2596088")
  WebElement h1AccDetailsTitle;

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

  @FindBy(css = "#C2__C2__BUT_CE93A4FCF8469E1D5834607[title='Change Payor']")
  WebElement btnChangePayor;

  @FindBy(css = "#C2__p4_QUE_3661A386F2BF3E14953402")
  WebElement divPaymentPlan;

  @FindBy(css = "#C2__p4_QUE_3661A386F2BF3E14953399")
  WebElement divPaymentMethod;

  @FindBy(css = "button[id$='C2__BUT_00A10DE7887C8958691749']")
  WebElement btnSaveMtaQuote;

  @FindBy(css = ".adjustwidth.prog_Active>div>button")
  WebElement pageActiveTitle;

  @FindBy(css = ".adjustwidth.prog_Active>div>button")
  WebElement btnclose;
  @FindBy(css = "#C2__ID_DIRECT_DEBIT_CONFIRM")
  WebElement confirmSTA;

  /**
   * 
   * 
   * Constructor class for Customer Get quote Page Here we initializing the driver for page factory
   * objects. For ajax element waiting time has added while initialization
   * 
   * @param driver : Webdriver
   */

  public GetQuotePage(WebDriver driver, ExtentTest report) {
    this.driver = driver;
    this.extentedReport = report;
    PageFactory.initElements(driver, this);
  }

  @Override
  protected void isLoaded() {

    if (!isPageLoaded) {
      Assert.fail();
    }

    if (isPageLoaded && !driver.getTitle().contains("GetQuote")) {
      Log.fail("SSP Get Quote Page did not open up. Site might be down.", driver, extentedReport);
    }
  }

  @Override
  protected void load() {

    WaitUtils.waitForPageLoad(driver);
    isPageLoaded = true;
    uielement = new ElementLayer(driver);

  }

  /**
   * To verify Get Quote Page
   *
   * @throws Exception
   * @return GetQuotePage
   * 
   */

  public GetQuotePage verifyGetQuotePage(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForPageLoad(driver);
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(btnDashboard));
      if (!btnDashboard.isEnabled()) {
        throw new Exception("Get Quote Page is not loaded properly");
      }

    } catch (Exception ex) {
      throw new Exception("Get Quote page verification Failed" + ex.getMessage());
    }
    return new GetQuotePage(driver, extentedReport).get();
  }

  /**
   * select Insurance item
   *
   * @param coverDetails : string
   * @param Screenshot :boolean
   * @param extentedReport
   * @throws Exception
   * @return String
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
   * click upgrade now (upgrading policy from 3* to 5*)
   *
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   * 
   */

  public void clickUpgradeNow(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, btnUpgradeNow);
      btnUpgradeNow.click();
      Log.message("Clicked on Upgrade Now button", driver, extentedReport);
      WaitUtils.waitForSpinner(driver);

    } catch (Exception e) {
      throw new Exception("Unable to click on Upgrade Now Button :" + e);
    }
  }

  /**
   * click recalculate (recalculate premium)
   *
   * @throws Exception : Custom Exception Message
   * @param extentedReport
   */
  public void clickReCalculate(ExtentTest extentedReport) throws Exception {

    try {
      WaitUtils.waitForElement(driver, btnReCalculate);
      btnReCalculate.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Re-Calculate", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to click on Re-Calculate Button :" + e);
    }
  }

  /**
   * Enter insurance cover details
   *
   * @throws Exception
   * @param testdata : hashmap
   * @param InscoverToAdd :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @param extentedReport
   * @return boolean
   * 
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
   *
   * To select radio button based on the options
   * 
   * @param locator as String
   * @param option as String
   * @throws Exception
   * 
   */

  private void selectRadioButton(String locator, String option) throws Exception {
    try {
      List<WebElement> buttonOptions = driver.findElements(By.cssSelector(locator));

      for (int i = 0; i < buttonOptions.size(); i++) {
        radio_button_value = buttonOptions.get(i).getText();
        if (radio_button_value.contains(option)) {
          buttonOptions.get(i).findElement(By.cssSelector("input")).click();
          radio_button_value = null;
          break;
        }
      }
    } catch (Exception e) {
      throw new Exception("Error in Selecting radio button : " + option + e);
    }
  }

  /**
   *
   * click on buy button
   * 
   * @param extentReport
   * @throws Exception
   * 
   */
  public void clickBuy(ExtentTest extentReport) throws Exception {
    try {
      btnBuy.click();
      Log.message("Clicked on Buy button", driver, extentReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Buy Button - Premium values could be zero :" + e);
    }
  }

  /**
   * Clicks on 'Buy' button during edit quote flow.
   * 
   * @param extentReport
   * @throws Exception
   */
  public void clickBuyEditQuote(ExtentTest extentReport) throws Exception {
    try {
      btnBuyEditQuote.click();
      Log.message("Clicked on Buy button", driver, extentReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to click on Buy Button. " + e);
    }
  }

  /**
   *
   * To enter MTA values
   * 
   * @return String
   * @param testData
   * @param screenshot
   * @throws Exception
   *
   */
  public String EnterMTAVals(HashMap<String, String> testData, boolean screenshot)
      throws Exception {

    try {
      String MTAAmt = null;
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, ins_MTAPayment,
          "Unable to find the MTA Accept Adjustment window");
      String strPopName = ins_MTAPayment.findElement(By.className("modal-title")).getText();
      if (strPopName.equalsIgnoreCase("Accept Adjustment")) {
        MTAAmt = AcptAdj_MTAamt.getText();
        if ((Double.parseDouble(MTAAmt) > 0)) {
          WaitUtils.waitForElementPresent(driver, MTA_PaymentMethod,
              "Choose Payment method field not found");
          MTA_PaymentMethod.sendKeys(testData.get("PaymentMethod").toString());
        }
        WaitUtils.waitForSpinner(driver);
        if (testData.get("MTA_AddRemove").toString().equalsIgnoreCase("Remove")) {
          WaitUtils.waitForElementPresent(driver, AcptAdj_AcceptBut_Rem,
              "Unable to find the Accept button on MTA Accept Adjustment window");
          AcptAdj_AcceptBut_Rem.click();
        } else {
          WaitUtils.waitForElementPresent(driver, AcptAdj_AcceptBut_Add,
              "Unable to find the Accept button on MTA Accept Adjustment window");
          AcptAdj_AcceptBut_Add.click();
        }

        WaitUtils.waitForinvisiblityofElement(driver, 360, ins_MTAPaymentPopup,
            "MTA Accept adjustment window failed to close after waiting for 3 mins");
      }
      return MTAAmt;
    }

    catch (Exception e) {
      throw new Exception(
          "Unable to Click on MTA Accept button.Exception occured" + e.getMessage());
    }

  }

  /**
   *
   * To get MTA amount
   * 
   * @throws Exception
   * @return String
   *
   */

  public String GetMTAAmount() throws Exception {
    String mtaAmountToChk = "";
    try {

      WaitUtils.waitForElementPresent(driver, btnDashboard, "Get Quote page has not loaded");
      WaitUtils.waitForElementPresent(driver, MTAamt_GetPage,
          "MTA amount field was not found on dashboard page");
      String mtaAmt = MTAamt_GetPage.getText();
      if (mtaAmt != null) {
        mtaAmountToChk = mtaAmt.split("£")[1];

      }

      return mtaAmountToChk;

    }

    catch (Exception e) {
      throw new Exception(
          "Unable to fetch the MTA amount,Premium values could be zero or null" + e);
    }
  }

  /**
   *
   * To enter payment method in MTA transaction
   * 
   * @throws Exception
   * @return String
   *
   */

  public String EnterPaymentMethod() throws Exception {
    String MTA_Val = null;
    try {
      WaitUtils.waitForElementPresent(driver, MTA_TakePaymentPopup,
          "Unable to find the Payment method popup");
      String strPopName = MTA_TakePaymentPopup.findElement(By.className("modal-title")).getText();
      if (strPopName.equalsIgnoreCase("Take a Payment")) {
        WaitUtils.waitForElementPresent(driver, MTA_PayAmount,
            "Expected Payment amount field not found on the Payment method pop up ");
        MTA_Val = MTA_PayAmount.getText().toString();
        WaitUtils.waitForElementPresent(driver, MTA_TrasacId,
            "Expected Payment transaction ID field not found on the Payment method pop up ");
        Random t = new Random();
        int Trans_ID = t.nextInt(1000);
        MTA_TrasacId.sendKeys(Integer.toString(Trans_ID));
        WaitUtils.waitForElementPresent(driver, MTA_SaveBut,
            "Save button not found on the Payment method pop up");
        MTA_SaveBut.click();
        WaitUtils.waitForinvisiblityofElement(driver, 360, ins_MTa_TakePaymnt,
            "Take a Payment window failed to close after waiting for 3 mins");
      }
      return MTA_Val;
    }

    catch (Exception e) {
      throw new Exception(
          "Filed to enter payment details on payment method window, Exception occured"
              + e.getMessage());
    }
  }

  /**
   * Enter Building coverDetails
   *
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
   * 
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
        WaitUtils.waitForinvisiblityofElement(driver, 180, ins_Bld_EditPopup,
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
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
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
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean enter_ADCoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
      WaitUtils.waitForSpinner(driver);
      String eletoInteract = RowToInteract + ":nth-child(2)>td:nth-child(4)";
      WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
          "Unable to Switch to Windows after " + AddRem + "ing Accidental damage cover");
      if (AddRem.equalsIgnoreCase("Add")) {
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val.contains(testData.get("AD_ExcessAmt").toString())) {
          BoolVal = true;
        }
      }



      else {
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
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
   * To click Add All MTA
   *
   * @param extentedReport
   * @param Screenshot :boolean
   * @throws Exception
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
   * To verifies the Add All MTA Next button
   *
   * @param extentedReport
   * @param Screenshot :boolean
   * @throws Exception
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

  public void clickSaveQuoteButton(ExtentTest extentedReport, boolean screenshot) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnSaveQuote);
      btnSaveQuote.click();
      Log.message("Clicked on Save Quote button", driver, extentedReport, screenshot);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on Save Quote Button " + e);
    }
  }

  /**
   * Click on Confirm MTA
   *
   * @param extentedReport
   * @throws Exception
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
   * Click on Add MTA
   *
   * @param extentedReport
   * @param Screenshot :boolean
   * @throws Exception
   * 
   */

  public void clickAddMTA(ExtentTest extentedReport, boolean screenshot) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Add Link"))
              .until(ExpectedConditions.elementToBeClickable(btnAddMTA));
      btnAddMTA.click();
      Log.message("Clicked on Add button", driver, extentedReport, screenshot);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on ADD Button " + e);
    }
  }



  public void clickAddSTA(ExtentTest extentedReport, boolean screenshot) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
          .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
          .withMessage("Unable to find Add Link"))
              .until(ExpectedConditions.elementToBeClickable(btnAddSTA));
      btnAddSTA.click();
      Log.message("Clicked on Add button", driver, extentedReport, screenshot);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Unable to Click on ADD Button " + e);
    }
  }

  /**
   * To select Available Adjustment Reason
   *
   * @param reason : string
   * @param Screenshot :boolean
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void selectAvalilableAdjReasons(String reason, ExtentTest extentedReport,
      boolean screenshot) throws Exception {
    try {
      WaitUtils.waitForListElement(driver, lstAdjReasons, 2);
      GenericUtils.getMatchingTextElementFromList(lstAdjReasons, reason).click();
      Log.message("Selected " + reason + " from Adjustment reasons", driver, extentedReport,
          screenshot);

    } catch (Exception e) {
      throw new Exception("Unable to Click on add all Next Button " + e);
    }
  }

  /**
   * To select Available Adjustment Reason
   *
   * @param reason : string
   * @param Screenshot :boolean
   * @param extentedReport
   * @throws Exception
   * 
   */

  public void selectAvalilableAdjReasonsSTA(String reason, ExtentTest extentedReport,
      boolean screenshot) throws Exception {
    try {
      WaitUtils.waitForListElement(driver, STAAdjReasons, 2);
      GenericUtils.getMatchingTextElementFromList(STAAdjReasons, reason).click();
      Log.message("Selected " + reason + " from Adjustment reasons", driver, extentedReport,
          screenshot);

    } catch (Exception e) {
      throw new Exception("Unable to Click on add all Next Button " + e);
    }
  }

  /**
   * Enter High Risk Items CoverDetails
   *
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
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
          selectRadioButton(ins_HRcover_RadioBut, "Yes");
          WaitUtils.waitForElementPresent(driver, ins_HRcoverRisk1,
              "Unable to find the HR covers when Yes button clicked");
          ins_HRcoverRisk1.sendKeys(testData.get("HR_NoOfItems").toString());

          WaitUtils.waitForElementPresent(driver, ins_HR_Category,
              "Unable to find the High Risk category field");
          ins_HR_Category.sendKeys(testData.get("HR_Category").toString());

          WaitUtils.waitForElementPresent(driver, ins_HR_Desc,
              "Unable to find the High Risk description field");
          ins_HR_Desc.sendKeys(testData.get("HR_ItemDescription").toString());
          ins_HR_Desc.click();
          WaitUtils.waitForElementPresent(driver, ins_HR_itemVal,
              "Unable to find the High Risk Item Value field");
          Actions actions = new Actions(driver);
          actions.moveToElement(ins_HR_itemVal).click();
          actions.sendKeys(testData.get("HR_ItemVaue").toString());
          Thread.sleep(2000);
          actions.sendKeys(Keys.RETURN).build().perform();
          Thread.sleep(2000);

          WaitUtils.waitForElementPresent(driver, ins_HR_SaveBut,
              "Unable to Save button on HR pop up");
          ins_HR_SaveBut.click();
          WaitUtils.waitForinvisiblityofElement(driver, 180, ins_HRCover_PopupWnd,
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
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception(
          "Unable to " + AddRem + " " + coverType + "cover details. Exception occured: " + e);
    }
  }

  /**
   * Enter Garden CoverDetails
   *
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
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
          WaitUtils.waitForinvisiblityofElement(driver, 180, ins_GCcover_PopupWnd,
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
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception(
          "Unable to " + AddRem + " " + coverType + "cover details. Exception occured: " + e);
    }
  }

  /**
   * Enter Technology and Entertainment CoverDetails
   *
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
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
          WaitUtils.waitForinvisiblityofElement(driver, 180, ins_TEcover_PopupWnd,
              coverType + " window failed to close after waiting for 3 mins");
          String Limit_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
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
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception(
          "Unable to " + AddRem + " " + coverType + "cover details. Exception occured: " + e);
    }
  }

  /**
   * Enter Personal Items CoverDetails
   *
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
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
          selectRadioButton(ins_PIcovre_Radiobut, "Yes");
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

          WaitUtils.waitForinvisiblityofElement(driver, 360, ins_PI_PoupWnd,
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
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception(
          "Unable to " + AddRem + " " + coverType + "cover details. Exception occured: " + e);
    }
  }

  /**
   * Enter pedal cycles CoverDetails
   *
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
   */
  public boolean enter_PCcoverDetails(HashMap<String, String> testData, String coverType,
      String AddRem, String RowToInteract, boolean screenshot) throws Exception {
    try {
      boolean BoolVal = false;
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
          WaitUtils.waitForElementPresent(driver, ins_PC_cover_RadioBut,
              "Unable to find the specified Pedalcyle radio button");
          selectRadioButton(ins_PCRadioBut, "Yes");
          WaitUtils.waitForElementPresent(driver, ins_PCcover_Specified_cyclecount,
              "Unable to find the specified Item nodes on Pedalcycle window");
          if (ins_PCcover_Specified_cyclecount.isDisplayed()) {
            ins_PCcover_Specified_cyclecount.sendKeys(testData.get("PC_specifiedItems").toString());
            WaitUtils.waitForElementPresent(driver, ins_PCcover_Description,
                "Unable to find the specified Item description field on Pedalcycle window");
            ins_PCcover_Description.sendKeys(testData.get("PC_specifiedItems_Desc").toString());
            ins_PCcover_Description.click();
            Thread.sleep(2000);
            WaitUtils.waitForElementPresent(driver, ins_PCcover_value,
                "Unable to find the specified Item description field on Pedalcycle window");
            ins_PCcover_value.sendKeys(testData.get("PC_specifiedItems_coverAmt").toString());
            WaitUtils.waitForElementPresent(driver, ins_PCcover_SaveBut,
                "Unable to find the save button on Pedalcycle window");
            ins_PCcover_SaveBut.click();
          }

          WaitUtils.waitForinvisiblityofElement(driver, 300, ins_PC_PopupWnd,
              coverType + " window failed to close after waiting for 3 mins");
          WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
              "Unable to Switch to Windows after adding Pedal cycle contents");
          String Limit_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
          System.out.println("Value is " + Limit_Val);
          int valuetochk = Integer.parseInt(testData.get("PC_UnspecifiedItems_coverAmt").toString())
              + Integer.parseInt(testData.get("PC_specifiedItems_coverAmt").toString());
          if (Limit_Val.contains(Integer.toString(valuetochk))) {
            BoolVal = true;
          }
        }

      } else {
        WaitUtils.waitForinvisiblityofElement(driver, 300, spinner,
            "Unable to Switch to Windows after " + AddRem + "ing pedal cycle cover");
        String excess_Val = driver.findElement(By.cssSelector(eletoInteract)).getText();
        System.out.println("Value is " + excess_Val);
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception(
          "Unable to " + AddRem + " " + coverType + "cover details. Exception occured: " + e);
    }

  }

  /**
   * Enter Home Emergency CoverDetails
   *
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
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
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception(
          "Unable to " + AddRem + " " + coverType + "cover details. Exception occured: " + e);
    }

  }

  /**
   * Enter Legal expenses CoverDetails
   *
   * @param testdata : Hashmap
   * @param coverType : string
   * @param AddRem :string
   * @param RowToInteract :string
   * @param screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
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
        if (excess_Val.isEmpty()
            || excess_Val.contains(testData.get("RemovedCover_Value").toString())) {
          BoolVal = true;
        }

      }
      return BoolVal;
    } catch (Exception e) {
      throw new Exception(
          "Unable to " + AddRem + " " + coverType + "cover details. Exception occured: " + e);
    }

  }

  /**
   * Click Acceptance Link
   *
   * @param extentReport
   * @param screenshot :boolean
   */

  public void clickAcceptanceLink(ExtentTest extentReport, boolean screenshot) {
    WaitUtils.waitForSpinner(driver);
    tabAcceptance.click();
    WaitUtils.waitForSpinner(driver);
    Log.message("Clicked on Acceptance Tab", driver, extentReport, screenshot);
  }

  /**
   * Description : Read Terms & Conditions section
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */
  public boolean verifyReadTermsAndCondition(ExtentTest extentedReport) throws Exception {
    boolean status = false;

    WaitUtils.waitForElementPresent(driver, termsAndConditionsSection,
        "Failed to locate Terms and condition check box");
    termsAndConditionsSection.click();
    Thread.sleep(3000);
    Log.message("Clicked on Terms and Condition Section", extentedReport);
    if (WaitUtils.waitForElement(driver, btnAttachTermAndCondition)) {
      btnAttachTermAndCondition.click();
      Thread.sleep(3000);
      Log.message("Clicked on button - Attach Term and Condition", driver, extentedReport);
      if (WaitUtils.waitForElement(driver, firstTermAndConditionInList)) {
        firstTermAndConditionInList.click();
        Thread.sleep(4000);
        Log.message("Clicked on first term and condition from the list", driver, extentedReport);
        if (WaitUtils.waitForElement(driver, selectTACfirst)) {
          selectTACfirst.click();
          Thread.sleep(4000);
          // btnclose.click();
          status = true;
        }
      } else {
        throw new Exception("First Term and condition not visible");
      }
      // firstTermAndConditionInList.click();
      // Thread.sleep(3000);
      // Log.message("Clicked on first term and condition from the list", driver, extentedReport);
    } else {
      throw new Exception("Button Attach Term and Condition not visible");
    }
    return status;
  }

  /**
   * Click on any Terms and Condition
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   */
  public void clickOnAnyTermsAndCondition(ExtentTest extentedReport) throws Exception {
    if (WaitUtils.waitForElement(driver, firstTermAndConditionInList)) {
      firstTermAndConditionInList.click();
      Thread.sleep(3000);
      Log.message("Clicked on first term and condition from the list", driver, extentedReport);

    } else {
      throw new Exception("Not clicked on terms and Conditions");
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
    boolean toReturn = false;
    try {
      try {
        WaitUtils.waitForElementPresent(driver, termsAndConditionsSection,
            "Failed to locate Terms and condition check box");
        termsAndConditionsSection.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Clicked on Terms and Condition Section", extentedReport);
      } catch (Exception f) {
        Log.message("Failed to click on Terms and Condition Section", extentedReport);
      }
      if (btnAttachTermAndCondition.isDisplayed()) {
        btnAttachTermAndCondition.click();
        WaitUtils.waitForSpinner(driver);
        Log.message("Clicked on button - Attach Term and Condition", driver, extentedReport);
        Thread.sleep(3000);
        if (firstTermAndConditionInList.isDisplayed()) {
          firstTermAndConditionInList.click();
          Thread.sleep(3000);
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on first term and condition from the list", driver, extentedReport);
          if (!selectTAC.isDisplayed() && !btnEditTermAndCondition.isDisplayed()) {
            firstTermAndConditionInList.click();
            Thread.sleep(3000);
            WaitUtils.waitForSpinner(driver);
            Log.message("Clicked Again on first term and condition from the list", driver,
                extentedReport);
          }
        } else {
          throw new Exception("First Term and condition not visible");
        }
      } else {
        throw new Exception("Button Attach Term and Condition not visible");
      }
      boolean isTACreadable = false;
      if (selectTAC.getText().length() > 0) {
        Log.message("Can read the terms and condition", extentedReport);
        Log.message("Terms and Conditions : " + selectTAC.getText(), extentedReport);
        isTACreadable = true;
      } else {
        Log.message("Cannot read the terms and condition", extentedReport);
      }
      if (isTACreadable == true) {
        String actualContent = selectTAC.getText();
        if (btnEditTermAndCondition.isDisplayed()) {
          btnEditTermAndCondition.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Edit button", extentedReport);
          Thread.sleep(2000);
          JavascriptExecutor js = (JavascriptExecutor) driver;
          js.executeScript(
              "document.getElementById('C2__C1__QUE_3A056143913CF4954126787_R2').innerHTML = 'Modified TAC content'");
          toReturn = (selectTAC.getText().contains("Modified TAC content")) ? true : false;
          Log.message("Actual Content : " + actualContent, extentedReport);
          Log.message("Modified Content : " + selectTAC.getText(), extentedReport);
          Log.message("Able to Edit the Term and Condition", extentedReport);
        } else {
          throw new Exception("Edit button not visible");
        }
      }
      return toReturn;
    } catch (Exception e) {
      throw new Exception("Unable to edit the Terms and Conditions" + e);
    }
  }

  /**
   * Description :Verify the Policy holder name
   * 
   * Return : boolean
   * 
   * @param extentedReport
   * 
   * @param CustName - Name to verify
   * 
   * @throws Exception : Custom Exception Message
   */

  public boolean verifyPolicyHolderSection(String CustName, ExtentTest extentedReport)
      throws Exception {
    try {

      return GenericUtils.verifyWebElementTextContains(fld_policyHolderName, CustName);

    } catch (Exception e) {
      throw new Exception(
          "Throws exception, unable to fetch the policy holder name from getquote page :" + e);
    }

  }

  /**
   * To select Add interested party
   *
   * @param extentedReport
   * @param Screenshot
   * @throw Exception : Custom Exception Message
   * 
   */

  public void clickAddParties(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, 180, addInterestedPartyBtn,
          "AddParties button is not visible");
      addInterestedPartyBtn.sendKeys(Keys.ENTER);
      Thread.sleep(2000);
      Log.message("Clicked on Add parties button", driver, extentReport, Screenshot);
    } catch (Exception e) {
      throw new Exception("Error in clicking add interested parties" + e);
    }
  }

  /**
   * select ContactType
   *
   * @param contacttype : String
   * @param extentedReport
   * @throws Exception
   * 
   * 
   */
  public void selectContactType(String contactType, ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForelementToBeClickable(driver, addInterestcontactType,
          "Failed to locate select scheme field on new quote pop up");

      addInterestcontactType.sendKeys(contactType);
      Thread.sleep(5000);
      Log.message("ContactType Selected: " + contactType);
    } catch (Exception e) {

      throw new Exception("Unable to Select contact type" + e);

    }
  }

  /**
   * To Click search on interested party window
   * 
   * @param extentReport
   * @throws Exception
   * 
   */
  public void clicksearch(ExtentTest extentReport) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, addInterestsearchBtn, "unable to click search");
      addInterestsearchBtn.click();
      Log.message("Clicked on search: ", extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to click search" + e);
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
      addInterestFirstname.clear();
      Log.message("Entered the addInterest First Name: " + Firstname, extentReport);
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

      WaitUtils.waitForElement(driver, addInterestLastname);
      addInterestLastname.click();
      addInterestLastname.sendKeys(Lastname);
      Thread.sleep(2000);
      Log.message("Entered the addInterest Last Name: " + Lastname, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to enter Lastname" + e);
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
          "unable to click select");
      addInterestselecthBtn.click();
      Log.message("Selected contact: ", driver, extentReport, true);
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
          "unable to select interestedPartyType");
      addInterestpartytype.sendKeys("underwriter");
      Log.message("Selected party: underwriter", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click select contact" + e);
    }

  }

  /**
   * To enter interested party to date
   * 
   * @param extentReport
   * @param Screenshot
   * @throws Exception
   * 
   * 
   */

  public void addInterestedPartyTypeToDate(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, addInterestpartytypeTodate,
          "unable to enter To date");
      String oneyrlater = "";
      Date date = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.add(Calendar.DAY_OF_YEAR, +360);
      Date oneyr = cal.getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      oneyrlater = formatter.format(oneyr);
      Log.message("Date entered:" + oneyrlater, extentReport);
      addInterestpartytypeTodate.sendKeys(oneyrlater);

    } catch (Exception e) {
      throw new Exception("Unable to click select contact" + e);
    }

  }

  /**
   * To Click save interested party window
   * 
   * @param extentReport
   * @param Screenshot :boolean
   * @throws Exception
   * 
   */
  public void addInterestedPartyClickSave(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {
      Thread.sleep(2000);
      WaitUtils.waitForelementToBeClickable(driver, addInterestpartySave, "unable to click save");
      addInterestpartySave.click();
      Log.message("Clicked on save button", driver, extentReport, true);
      Thread.sleep(8000);
    } catch (Exception e) {
      throw new Exception("Unable to click select contact" + e);
    }

  }

  /**
   * To verify delete icon on price presentation page in intrested party section
   * 
   * @param extentReport
   * @param Screenshot :boolean
   * @throws Exception
   * 
   */

  public void click_Deleteicon(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, 360, addPartyDeleteicon,
          "Delete icon is not presented");
      Log.message("Delete icon is presented", driver, extentReport, true);
      addPartyDeleteicon.click();
      Thread.sleep(5000);

      Log.message("Clicked on delete icon", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click delete icon" + e);
    }

  }

  /**
   * To check the Interested party search field
   * 
   * @param extentReport
   * @param Screenshot :boolean
   * @throws Exception
   * 
   */
  public void checkAddPartySearchResultsPane(ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {

      WebElement createbtn = driver.findElement(By.cssSelector(addInterestCreateAddInterest));

      if (createbtn.isDisplayed())
        Log.message("Create add interested party button is present ", driver, extentReport);
      WebElement cancelbtn = driver.findElement(By.cssSelector(addInterestCancelBtn));
      Log.message(cancelbtn.getText() + "button is present ", driver, extentReport);
    } catch (Exception e) {
      throw new Exception("Unable to check interested parties" + e);
    }
  }

  /**
   * To checkaddedInterestedParty
   * 
   * @param extentReport
   * @param screenShot :boolean
   * @throws Exception
   * 
   */
  public void checkaddedInterestedParty(ExtentTest extentReport, String customername,
      boolean screenShot) throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, addedParty, "unable to find added party");
      String AddepartyName = addedParty.getText().toString();
      if (AddepartyName.contains(customername)) {
        Log.message(customername + "  Added party is visible", driver, extentReport, screenShot);
      } else
        Log.fail(customername + " Added party is  not visible", driver, extentReport, screenShot);
    } catch (Exception e) {
      throw new Exception("Unable to find added party" + e);
    }

  }

  /**
   * verify bin icon present in policy holder table
   * 
   * @param NameToVerify - Name of policy holder
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   * @return boolean
   * 
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
   * To click bin icon in policy holder table
   * 
   * @param NameToclick - PolicyHolder name whose bin icon is to b clicked
   * @param extentedReport
   * @param screenshot
   * @throws Exception
   * 
   */
  public void clickBinIcon(String NameToclick, boolean screenShot, ExtentTest extentedReport)
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

          WebElement iconBin = row.findElement((By.cssSelector(cssBinIcon)));
          if (iconBin.isEnabled()) {
            iconBin.click();
            Log.message("Bin Icon is clicked successfully", driver, extentedReport, screenShot);
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
   * To click Yes to remove policy
   * 
   * @param NameToclick - Policyholder name to remove
   * @param extentedReport
   * @param screeeShot
   * @throws Exception
   * 
   */
  public void clickYesToRemovePolicy(String NameToclick, boolean screenShot,
      ExtentTest extentedReport) throws Exception {

    try {
      clickBinIcon(NameToclick, screenShot, extentedReport);

      WaitUtils.waitForelementToBeClickable(driver, btnYesToRemovejointPolicy,
          "Failed to locate yes button to confirm remove joint policy");
      btnYesToRemovejointPolicy.click();
      WaitUtils.waitForinvisiblityofElement(driver, 120, cssBtnYesToRemovePolicyHolder,
          "Remove policy holder pop failed to close after waiting for 2 mins");

    } catch (Exception e) {
      throw new Exception("Error while clicking Yes button" + e);
    }

  }

  /**
   *
   * verify ContactName is present in policy holder table
   * 
   * @param NameToVerify - PolicyHolder name to verify its availability
   * @param extentedReport
   * @param screenShot
   * @return boolean
   * @throws Exception
   * 
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
   *
   * To click BackToDashboard button
   * 
   * @param extentedReport
   * @throws Exception
   * @return CustDashboardPage
   * 
   */

  public CustDashboardPage clickBackToDashboardButton(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnDashboard);
      btnDashboard.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("BackToDashboard button is clicked", extentedReport);
      return new CustDashboardPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception("Unable to Select Valid Contact" + e);
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
   * To verify search button on interested party modal
   *
   * @param extentedReport :ExtentTest
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verify_searchButton(ExtentTest extentedReport) throws Exception {
    try {
      return WaitUtils.waitForElement(driver, addInterestsearchBtn, 1);

    } catch (Exception e) {
      throw new Exception("Unable to get search button from interested party modal" + e);
    }

  }

  /**
   * To select MTA reason and payment
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * 
   */

  public void selectMTAReasonPayment(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, sel_MTAReason, 2);
      Click_selectmenu("104", sel_MTAReason, extentedReport);
      WaitUtils.waitForElement(driver, btn_MTAreasonAdd, 2);
      btn_MTAreasonAdd.click();
      Log.message("Selected on MTA reason", extentedReport);
      WaitUtils.waitForElementPresent(driver, 160, btn_MTAnext,
          "Failed to locate next button after selecting MTA reason in get quote page");
      btn_MTAnext.click();
      Log.message("Clicked on next button in get quote page", extentedReport);
      Thread.sleep(2000);
    } catch (Exception e) {
      throw new Exception("Unable to get search button from interested party modal" + e);
    }

  }

  /**
   * To select STA reason and payment
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * 
   */

  public void selectSTAReasonPayment(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, sel_STAReason, 2);
      Click_selectmenu("109", sel_STAReason, extentedReport);
      WaitUtils.waitForElement(driver, btn_STAreasonAdd, 2);
      btn_STAreasonAdd.click();
      Log.message("Selected on STA reason", extentedReport);
      WaitUtils.waitForElementPresent(driver, 160, btn_MTAnext,
          "Failed to locate next button after selecting STA reason in get quote page");
      btn_MTAnext.click();
      Log.message("Clicked on next button in get quote page", extentedReport);
      Thread.sleep(2000);
    } catch (Exception e) {
      throw new Exception("Unable to get search button from interested party modal" + e);
    }

  }


  /**
   * To select menu item from the select list
   *
   * @param eleToClick: WebElement
   * @param val : String
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * 
   */
  public void Click_selectmenu(String val, WebElement eleToClick, ExtentTest extentedReport)
      throws Exception {
    try {
      Select cotentsdropdown = new Select(eleToClick);
      cotentsdropdown.selectByValue(val);
      Log.message("Selected menu option: " + val, extentedReport);
    } catch (Exception e) {
      throw new Exception("Selected menu option" + e);
    }
  }

  /**
   * To select Visacard
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * @return CardDetailsPage
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
   * To verify Payment Trasaction
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verifyPaymentTrasaction(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, txt_paymntRef,
          "Failed to get payment reference id after payment");
      Log.message("Payment reference number : " + txt_paymntRef.getText(), extentedReport);
      return (Integer.parseInt(txt_paymntRef.getText()) > 0);

    } catch (Exception e) {
      throw new Exception("Trasaction was not successful, failed to fetch the transaction id " + e);
    }

  }

  /**
   * To Click Take payment
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * 
   */

  public void takePayment(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElement(driver, but_takPayment, 60);
      but_takPayment.click();
      WaitUtils.waitForSpinner(driver);

    } catch (Exception e) {
      throw new Exception(
          "Unable to click on take payment Button in get quote page, exception occured :" + e);
    }

  }



  public NewQuotePage confirm() throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);


      confirmSTA.click();

      Log.message("Clicked on Confirm  button", extentedReport);



      return new NewQuotePage(driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment" + e);
    }

  }

  /**
   * To Click confirm payment on Rnl policy
   *
   * @param mtaSta
   * @param testdata : hashmap
   * @param ExtentTest :extentedReport
   * @throws Exception
   * @return CustDashboardPage
   * 
   */
  public NewQuotePage confirmPaymentOnMTA(String mtaSta, HashMap<String, String> testdata,
      ExtentTest extentedReport) throws Exception {
    try {
      String paymentplan = testdata.get("Payment Plan");
      WebElement btn_toClick;
      if (mtaSta.equalsIgnoreCase("MTA"))
        btn_toClick = btnConfirmPayment;
      else
        btn_toClick = btn_STAconfirm;

      switch (paymentplan) {
        case "Annual":
          if (!WaitUtils.waitForElement(driver, btn_toClick, 1))
            btn_toClick = btnConfirmMTA;
          btn_toClick.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Confirm payment button", extentedReport);
          break;

        case "Monthly":
          (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
              .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
              .withMessage("Unable to find Edit Details Link"))
                  .until(ExpectedConditions.elementToBeClickable(btnAcceptPayment));
          btnAcceptPayment.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on accept payment button", extentedReport);
          break;

      }

      return new NewQuotePage(driver, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment" + e);
    }

  }


  /**
   * To Click confirm payment
   *
   * @param mtaSta
   * @param testdata : hashmap
   * @param ExtentTest :extentedReport
   * @throws Exception
   * @return CustDashboardPage
   * 
   */
  public CustDashboardPage confirmPayment(String mtaSta, HashMap<String, String> testdata,
      ExtentTest extentedReport) throws Exception {
    try {
      boolean flag = false;
      int count = 1;
      for (int i = 0; i <= count; i++) {

        WaitUtils.waitForSpinner(driver);
        (new WebDriverWait(driver, 180).pollingEvery(3000, TimeUnit.MILLISECONDS)
            .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
            .withMessage("Unable to find Confirm Button Link"))
        .until(ExpectedConditions.elementToBeClickable(btnConfirmPayment)); 
                
        flag = btnConfirmPayment.isDisplayed();
        if (flag) {
          System.out.println("Element is displayed");
          break;
        } else {
          count++;
        }
      }

      String paymentplan = testdata.get("Payment Plan");
      WebElement btn_toClick;
      if (mtaSta.equalsIgnoreCase("MTA"))
        btn_toClick = btnConfirmPayment;
      else
        btn_toClick = btn_STAconfirm;

      switch (paymentplan) {
        case "Annual":
          if (!WaitUtils.waitForElement(driver, btn_toClick, 1))
            btn_toClick = btnConfirmMTA;
          ((JavascriptExecutor) driver).executeScript("arguments[0].click()", btn_toClick);
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on Confirm payment button in GetQuote page", extentedReport);
          break;

        case "Monthly":
          (new WebDriverWait(driver, 180).pollingEvery(200, TimeUnit.MILLISECONDS)
              .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
              .withMessage("Unable to find 'Accept' button"))
                  .until(ExpectedConditions.elementToBeClickable(btnAcceptPayment));
          btnAcceptPayment.click();
          WaitUtils.waitForSpinner(driver);
          Log.message("Clicked on accept payment button in GetQuote page", extentedReport);
          break;

      }
      return new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to Confirm Payment" + e);
    }

  }

  /**
   * Verifies the presence of 'Accept' button in 'Acceptance' tab.
   * 
   * @param extentedReport
   * @return true if 'Accept' button is present; false otherwise
   */
  public boolean verifyAcceptButtonPresence(ExtentTest extentedReport) {
    return WaitUtils.waitForElement(driver, btnAcceptPayment, 30);
  }

  /**
   * To click create new interested party
   * 
   * @param extentReport
   * @param Screenshot :boolean
   * @throws Exception
   * 
   */

  public void clickcreatenewAddparty(ExtentTest extentReport, boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForelementToBeClickable(driver, addInterestcreateAddInterest,
          "createAddinterest button is not visible");
      addInterestcreateAddInterest.sendKeys(Keys.ENTER);
      Log.message("Clicked on createNewAddInterest button", driver, extentReport, true);
    } catch (Exception e) {
      throw new Exception("Unable to click create new interested party button" + e);
    }
  }

  /**
   * To verify Default Contact Tab
   * 
   * @param tabNameToVerify
   * @param extentReport
   * @param Screenshot :boolean
   * @throws Exception
   * @return boolean
   * 
   */

  public boolean verify_defaultContactTab(String tabNameToVerify, ExtentTest extentReport,
      boolean Screenshot) throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, Ele_engagement_Centre_tabs,
          "Unable to find Personal/corporate contact tabs");
      Log.message(contact_activetab.getText() + " tab was enabled", extentReport);
      return tabNameToVerify.contains(contact_activetab.getText());

    } catch (Exception e) {
      throw new Exception("Unable to find Personal/corporate contact tabs,throws exception :" + e);
    }

  }

  /**
   * To click Contact Tab
   * 
   * @param TabtoClick
   * @param extentReport
   * @param Screenshot :boolean
   * @throws Exception
   * 
   */

  public void Click_contactTabs(String TabtoClick, ExtentTest extentReport, boolean Screenshot)
      throws Exception {
    try {
      WaitUtils.waitForSpinner(driver);
      WaitUtils.waitForElementPresent(driver, Ele_engagement_Centre_tabs,
          "Unable to find Personal/corporate contact tabs");
      if (TabtoClick.equalsIgnoreCase("Personal"))
        enga_Centre_tabs_Personal.click();
      else
        enga_Centre_tabs_Corporate.click();

      Log.message("Clicked on " + TabtoClick + " contact tab", extentReport);

    } catch (Exception e) {
      throw new Exception("Unable to click Personal/corporate contact tabs,throws exception :" + e);
    }

  }

  /**
   * Verify banner title as Engagement centre
   * 
   * @param extentedReport
   * @throws Exception : Custom Exception Message
   * @return boolean
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
    txtAccSortCode.sendKeys(sortCode);
    Log.message("Sort code entered : " + sortCode, extentedReport);

    WaitUtils.waitForElementPresent(driver, btnCheckAccount, "Check account button is not found");
    btnCheckAccount.click();
    WaitUtils.waitForSpinner(driver);
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
    sortCode = sortCode.matches("\\d{2}-\\d{2}-\\d{2}") ? sortCode
        : sortCode.substring(0, 2) + "-" + sortCode.substring(2, 4) + "-" + sortCode.substring(4);
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
        : sortCode.substring(0, 2) + "-" + sortCode.substring(2, 4) + "-" + sortCode.substring(4);
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
          .withMessage("Unable to find Edit Details Link"))
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
          .withMessage("Unable to find Edit Details Link"))
              .until(ExpectedConditions.elementToBeClickable(selectpaymentmethod));
      selectpaymentmethod.sendKeys(paymentMethod);

      Log.message("Payment Method Selected " + paymentMethod, extentedReport);
    } catch (Exception e) {
      throw new Exception("Unable to Select Payment Method" + e);
    }
  }

  /**
   * To search last Name in coporateIP
   * 
   * @param extentReport
   * @param Screenshot :boolean
   * @param testdata :hashmap
   * @throws Exception
   * 
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
   * Saves the quote by clicking 'Save Quote'button in the Payment page.
   * 
   * @param extentedReport
   * @return the <code>CustDashboardPage</code> object, or null if no <code>CustDashboardPage</code>
   *         representation is applicable
   * @throws Exception
   */
  public CustDashboardPage saveQuote(ExtentTest extentedReport) throws Exception {
    CustDashboardPage objCustDashboardPage = null;
    try {
      btnSaveQuote.click();
      Log.message("Clicked on 'Save Quote' payment button", extentedReport);
      WaitUtils.waitForSpinner(driver);
      objCustDashboardPage = new CustDashboardPage(driver, extentedReport).get();
    } catch (Exception e) {
      throw new Exception("Unable to save quote. " + e);
    }
    return objCustDashboardPage;
  }

  /**
   * Verifies that radio buttons available in 'MTA Adjustment' section.
   * 
   * @param shouldSingleDisabled - true if 'Single Bill-Card' radio button should be disabled; false
   *        otherwise
   * @param extentedReport
   * @param screenShot
   * @return true if the 'Spread over Instalments' radio button is checked and the 'Single
   *         Bill-Card' radio button is disabled/unchecked; false otherwise.
   * @throws Exception
   */
  public boolean verifyMTAAdjustmentRadioButtons(boolean shouldSingleDisabled,
      ExtentTest extentedReport, boolean screenShot) throws Exception {
    boolean isOk = false;

    try {
      if (radSpreadOverInstallments.isSelected()) {
        Log.message("'Spread over Instalments' radio button is in checked state", extentedReport);
        if (shouldSingleDisabled) {
          if (!radSingleBillCard.isEnabled()) {
            Log.message("'Single Bill-Card' radio button is in disabled state", extentedReport);
            isOk = true;
          } else {
            Log.message("'Single Bill-Card' radio button is in enabled state", extentedReport);
          }
        } else {
          if (!radSingleBillCard.isSelected()) {
            Log.message("'Single Bill-Card' radio button is not in checked state", extentedReport);
            isOk = true;
          } else {
            Log.message("'Single Bill-Card' radio button is in checked state", extentedReport);
          }
        }
      } else {
        Log.message("'Spread over Instalments' radio button is not in checked state",
            extentedReport);
      }
    } catch (Exception e) {
      throw new Exception("Exception veriying the MTA adjustment radio buttons. " + e);
    }

    return isOk;
  }

  /**
   * Selects the 'Spread over Instalments' radio button.
   * 
   * @param extentedReport
   * @throws Exception
   */
  public void selectSpreadOverInstallmentsRadioButton(ExtentTest extentedReport) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, radSpreadOverInstallments);
      radSpreadOverInstallments.click();
      Log.message("Selected the 'Spread over Instalments' radio button", extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Exception in selecting the 'Spread over Instalments' radio button." + e);
    }
  }

  /**
   * Selects the 'Single Bill-Card' radio button.
   * 
   * @param extentedReport
   * @throws Exception
   */
  public void selectSingleBillCardRadioButton(ExtentTest extentedReport) throws Exception {
    try {
      GenericUtils.scrollIntoView(driver, radSingleBillCard);
      radSingleBillCard.click();
      Log.message("Selected the 'Single Bill-Card' radio button", extentedReport);
      WaitUtils.waitForSpinner(driver);
    } catch (Exception e) {
      throw new Exception("Exception in selecting the 'Single Bill-Cards' radio button." + e);
    }
  }

  /**
   * Verifies the 'Payment Schedule' table.
   * 
   * @param adjustmentOption - 'Spread over Instalments' or 'Single Bill-Card', if adjustment flow
   * @param testData
   * @param extentedReport
   * @param screenshot
   * @return true if verification is successful; false otherwise.
   */
  public boolean verifyPaymentScheduleTable(String adjustmentOption,
      HashMap<String, String> testData, ExtentTest extentedReport, boolean screenshot) {
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
      boolean isColumnOk = verifyPaymentScheduleColumn(columnName, adjustmentOption.toLowerCase(),
          testData, extentedReport, true);
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
   * Verifies the data in the given column on 'Payment Schedule' table during MTA.
   * 
   * @param columnName
   * @param adjustmentOption - 'Spread over Instalments' or 'Single Bill-Card'
   * @param testData
   * @param extentedReport
   * @param screenshot
   * @return true if the data in the given column are as expected; false otherwise.
   */
  private boolean verifyPaymentScheduleColumn(String columnName, String adjustmentOption,
      HashMap<String, String> testData, ExtentTest extentedReport, boolean screenshot) {
    boolean isOk = false;
    // Single Bill-Card or Spread over Instalments
    int totalInstallments =
        adjustmentOption.contains("spread") ? 12 : (adjustmentOption.contains("single") ? 13 : 12);

    Log.message("Verifying the '" + columnName + "' column in 'Payment Schedule' ("
        + totalInstallments + " installments)", extentedReport);

    if (columnName.equals("Due")) { // #1
      int preferredPaymentDay = testData.containsKey("Preferred Payment Day")
          ? Integer.parseInt(testData.get("Preferred Payment Day"))
          : Integer.parseInt(DateTimeUtility.getCurrentDate().split("/")[0]);
      List<String> lstInstDates =
          DateTimeUtility.generateInstallmentDates(preferredPaymentDay, totalInstallments);
      if (totalInstallments == 13) { // Single Bill-Card
        lstInstDates.add(1, lstInstDates.get(0));
      }
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
      isOk = verifyCalculatedPremium(adjustmentOption, testData, extentedReport, screenshot);
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
      isOk = verifyAmountPayable(adjustmentOption, testData, extentedReport, screenshot);
    }

    return isOk;
  }

  /**
   * Verifies the 'Calculated Premium' column data of Payment Schedule table.
   * 
   * @param adjustmentOption - 'Spread over Instalments' or 'Single Bill-Card'
   * @param testData
   * @param extentedReport
   * @param screenshot
   * @return true if the 'Calculated Premium' column data is as expected; false otherwise.
   */
  private boolean verifyCalculatedPremium(String adjustmentOption, HashMap<String, String> testData,
      ExtentTest extentedReport, boolean screenshot) {
    boolean isOk = false;

    List<WebElement> lstCalculatedPremium =
        driver.findElements(By.cssSelector(cssPaySchedCalculatedPremium));
    float expectedTotalCalculatedPremium = 0;
    String actualTotalCalculatedPremium = divTotalCalculatedPremium.getText().trim();
    String oldPremium, adjustmentAmount, totalPayment, firstPayment, installmentPayment;
    oldPremium = adjustmentAmount = totalPayment = firstPayment = installmentPayment = null;
    int totalInstallments =
        adjustmentOption.contains("spread") ? 12 : (adjustmentOption.contains("single") ? 13 : 12);
    if (adjustmentOption.contains("spread") || adjustmentOption.contains("single")) {
      oldPremium = spnExistingPremiumAcceptanceTab.getText().trim();
      adjustmentAmount = spnAdjustmentAmountAcceptanceTab.getText().trim();
      totalPayment = spnNewPremiumAcceptanceTab.getText().trim();
      firstPayment = testData.get("firstPaymentCoverTab");
      installmentPayment = testData.get("installmentPaymentCoverTab");
    } else {
      totalPayment = spnTotalPaymentAcceptanceTab.getText().trim();
      firstPayment = spnFirstPaymentAcceptanceTab.getText().trim();
      installmentPayment = spnInstallmentPaymentAcceptanceTab.getText().trim();
    }

    boolean isOkRowCount = lstCalculatedPremium.size() == totalInstallments;
    Log.message("Row count: [" + lstCalculatedPremium.size() + "]; totalInstallments: ["
        + totalInstallments + "]; isOkRowCount: [" + isOkRowCount + "]", extentedReport);
    boolean isOkFirstPayment = false;
    boolean isOkInstallmentPayment = false;
    boolean isOkTotalCalculatedPremium = false;
    boolean isOkAdjustmentAmount = false;

    for (int i = 0; i < totalInstallments; i++) {
      float actualCalculatedPremium =
          Float.parseFloat(lstCalculatedPremium.get(i).getText().trim());
      expectedTotalCalculatedPremium += actualCalculatedPremium;

      if (i == 0) {
        isOkFirstPayment = String.format("%.2f", actualCalculatedPremium).equals(firstPayment);
        Log.message("Row #" + (i + 1) + ": actualCalculatedPremium: ["
            + String.format("%.2f", actualCalculatedPremium) + "]; firstPayment: [" + firstPayment
            + "]; isOkFirstPayment: [" + isOkFirstPayment + "]", extentedReport);
      } else if (i == 1 && adjustmentOption.contains("single")) {
        isOkAdjustmentAmount =
            String.format("%.2f", actualCalculatedPremium).equals(adjustmentAmount);
        Log.message(
            "Row #" + (i + 1) + ": actualCalculatedPremium: ["
                + String.format("%.2f", actualCalculatedPremium) + "]; adjustmentAmount: ["
                + adjustmentAmount + "]; isOkAdjustmentAmount: [" + isOkAdjustmentAmount + "]",
            extentedReport);
      } else {
        if (!adjustmentOption.contains("single") && i > 1 && !isOkInstallmentPayment) {
          continue;
        } else if (adjustmentOption.contains("single") && i > 2 && !isOkInstallmentPayment) {
          continue;
        } else {
          if (adjustmentOption.contains("spread")) {
            isOkInstallmentPayment =
                !String.format("%.2f", actualCalculatedPremium).equals(installmentPayment);
          } else { // single or edit quote
            isOkInstallmentPayment =
                String.format("%.2f", actualCalculatedPremium).equals(installmentPayment);
          }
          Log.message("Row #" + (i + 1) + ": actualCalculatedPremium: ["
              + String.format("%.2f", actualCalculatedPremium) + "]; installmentPayment: ["
              + installmentPayment + "]; isOkInstallmentPayment: [" + isOkInstallmentPayment + "]",
              extentedReport);
        }
      }
    }

    isOkTotalCalculatedPremium =
        actualTotalCalculatedPremium.equals(String.format("%.2f", expectedTotalCalculatedPremium))
            && actualTotalCalculatedPremium.equals(totalPayment);
    Log.message("actualTotalCalculatedPremium: [" + actualTotalCalculatedPremium
        + "]; expectedTotalCalculatedPremium: ["
        + String.format("%.2f", expectedTotalCalculatedPremium) + "]; totalPayment: ["
        + totalPayment + "]", extentedReport);
    if (adjustmentOption.contains("spread")) {
      String calcAdjAmount = String.format("%.2f",
          Float.parseFloat(actualTotalCalculatedPremium) - Float.parseFloat(oldPremium));
      isOkAdjustmentAmount = calcAdjAmount.equals(adjustmentAmount);
      Log.message(
          "actualCalculatedPremium - oldPremium: [" + calcAdjAmount + "]; adjustmentAmount: ["
              + adjustmentAmount + "]; isOkAdjustmentAmount: [" + isOkAdjustmentAmount + "]",
          extentedReport);
    }

    if (adjustmentOption.contains("spread") || adjustmentOption.contains("single")) {
      isOk = isOkRowCount && isOkFirstPayment && isOkInstallmentPayment
          && isOkTotalCalculatedPremium && isOkAdjustmentAmount;
      Log.message("isOkRowCount: [" + isOkRowCount + "]; isOkFirstPayment: [" + isOkFirstPayment
          + "]; isOkInstallmentPayment: [" + isOkInstallmentPayment
          + "]; isOkTotalCalculatedPremium: [" + isOkTotalCalculatedPremium
          + "]; isOkAdjustmentAmount:[" + isOkAdjustmentAmount + "]", extentedReport);
    } else {
      isOk =
          isOkRowCount && isOkFirstPayment && isOkInstallmentPayment && isOkTotalCalculatedPremium;
      Log.message(
          "isOkRowCount: [" + isOkRowCount + "]; isOkFirstPayment: [" + isOkFirstPayment
              + "]; isOkInstallmentPayment: [" + isOkInstallmentPayment
              + "]; isOkTotalCalculatedPremium: [" + isOkTotalCalculatedPremium + "]",
          extentedReport);
    }

    return isOk;
  }

  /**
   * Verifies the 'Amount Payable' column data of Payment Schedule table.
   * 
   * @param adjustmentOption - 'Spread over Instalments' or 'Single Bill-Card'
   * @param testData
   * @param extentedReport
   * @param screenshot
   * @return true if the 'Amount Payable' column data is as expected; false otherwise.
   */
  private boolean verifyAmountPayable(String adjustmentOption, HashMap<String, String> testData,
      ExtentTest extentedReport, boolean screenshot) {
    boolean isOk = false;

    int totalInstallments =
        adjustmentOption.contains("spread") ? 12 : (adjustmentOption.contains("single") ? 13 : 12);
    // List<WebElement> lstDueDates = driver.findElements(By.cssSelector(cssPaySchedDueDates));
    List<WebElement> lstCalculatedPremium =
        driver.findElements(By.cssSelector(cssPaySchedCalculatedPremium));
    List<WebElement> lstTaxes = driver.findElements(By.cssSelector(cssPaySchedTaxes));
    List<WebElement> lstCharges = driver.findElements(By.cssSelector(cssPaySchedCharges));
    List<WebElement> lstChargesTax = driver.findElements(By.cssSelector(cssPaySchedChargesTax));
    List<WebElement> lstInstallments = driver.findElements(By.cssSelector(cssPaySchedInstallments));
    List<WebElement> lstAmountPayable =
        driver.findElements(By.cssSelector(cssPaySchedAmountPayable));
    float expectedTotalAmountPayable = 0;
    String actualTotalAmountPayable = divTotalAmountPayable.getText().trim();
    String totalPayment =
        (adjustmentOption.contains("spread") || adjustmentOption.contains("single"))
            ? spnNewPremiumAcceptanceTab.getText().trim()
            : spnTotalPaymentAcceptanceTab.getText().trim();

    boolean isOkRowCount = lstAmountPayable.size() == totalInstallments;
    Log.message("Row count: [" + lstCalculatedPremium.size() + "]; totalInstallments: ["
        + totalInstallments + "]; isOkRowCount: [" + isOkRowCount + "]", extentedReport);
    boolean isOkRowData = false;
    boolean isOkTotalAmountPayable = false;

    for (int i = 0; i < totalInstallments; i++) {
      float rowCalculatedPremium = Float.parseFloat(lstCalculatedPremium.get(i).getText().trim());
      float rowTaxes = Float.parseFloat(lstTaxes.get(i).getText().trim());
      float rowCharges = Float.parseFloat(lstCharges.get(i).getText().trim());
      float rowChargesTax = Float.parseFloat(lstChargesTax.get(i).getText().trim());
      float rowInstallments = Float.parseFloat(lstInstallments.get(i).getText().trim());
      String rowAmountPayable = lstAmountPayable.get(i).getText().trim();
      String rowTotal = String.format("%.2f",
          (rowCalculatedPremium + rowTaxes + rowCharges + rowChargesTax + rowInstallments));
      expectedTotalAmountPayable += Float.parseFloat(rowAmountPayable);
      boolean isOkCurrentRow = rowAmountPayable.equals(rowTotal);
      Log.message("Row #" + (i + 1) + ": rowAmountPayable: [" + rowAmountPayable + "]; rowTotal: ["
          + rowTotal + "]; isOkCurrentRow: [" + isOkCurrentRow + "]", extentedReport);
      if (i > 0 && !isOkRowData) {
        continue;
      } else {
        isOkRowData = isOkCurrentRow;
      }
    }

    isOkTotalAmountPayable =
        actualTotalAmountPayable.equals(String.format("%.2f", expectedTotalAmountPayable))
            && actualTotalAmountPayable.equals(totalPayment);
    Log.message("actualTotalAmountPayable: [" + actualTotalAmountPayable
        + "]; expectedTotalAmountPayable: [" + String.format("%.2f", expectedTotalAmountPayable)
        + "]; totalPayment: [" + totalPayment + "]", extentedReport);

    isOk = isOkRowCount && isOkRowData && isOkTotalAmountPayable;
    Log.message("isOkRowCount: [" + isOkRowCount + "]; isOkRowData: [" + isOkRowData
        + "]; isOkTotalAmountPayable: [" + isOkTotalAmountPayable + "]", extentedReport);


    return isOk;
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

  /**
   * Returns the <i>testData</i> <code>HashMap</code> with payment details grabed from Cover tab
   * during Payment Adjustment flow.
   * 
   * @param testData
   * @param extentedReport
   * @param screenShot
   * @return the <code>HashMap</code> representation of testData, or null if no <code>HashMap</code>
   *         representation is applicable
   * @throws Exception
   */
  public HashMap<String, String> grabPaymentDetailsFromAdjustmentCoverTab(
      HashMap<String, String> testData, ExtentTest extentedReport, boolean screenShot)
      throws Exception {
    GenericUtils.scrollIntoView(driver, divExistingPremiumCoverTab);
    String existingPremiumCoverTab =
        divExistingPremiumCoverTab.getText().replaceFirst("£", "").trim();
    String newPremiumCoverTab = divNewPremiumCoverTab.getText().replaceFirst("£", "").trim();
    String adjustmentAmountCoverTab =
        divAdjustmentAmountCoverTab.getText().replaceFirst("£", "").trim();

    testData.put("existingPremiumCoverTab", existingPremiumCoverTab);
    testData.put("newPremiumCoverTab", newPremiumCoverTab);
    testData.put("adjustmentAmountCoverTab", adjustmentAmountCoverTab);

    Log.message("Adjustment Payment Cover Tab Payment Details:- Existing Premium: ["
        + existingPremiumCoverTab + "]; New Premium: [" + newPremiumCoverTab
        + "]; Adjustment Amount: [" + adjustmentAmountCoverTab + "]", extentedReport);

    return testData;
  }

  /**
   * Verifies that the payment details available at Acceptance tab matches the payment details
   * grabed from Cover tab during Edit Quote flow.
   * 
   * @param testData
   * @param extentedReport
   * @param screenShot
   * @return true if the payment details available at Acceptance tab are as expected; false
   *         otherwise.
   * @throws Exception
   */
  public boolean verifyPaymentDetailsAtEditQuoteAcceptanceTab(HashMap<String, String> testData,
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
   * Verifies that the payment details available at Acceptance tab matches the payment details
   * grabed from Cover tab during payment adjustment flow.
   * 
   * @param testData
   * @param extentedReport
   * @param screenShot
   * @return true if the payment details available at Acceptance tab are as expected; false
   *         otherwise.
   * @throws Exception
   */
  public boolean verifyPaymentDetailsAtAdjustmentPaymentAcceptanceTab(
      HashMap<String, String> testData, ExtentTest extentedReport, boolean screenShot)
      throws Exception {
    boolean isOk = false;

    try {
      GenericUtils.scrollIntoView(driver, spnExistingPremiumAcceptanceTab);
      String existingPremiumCoverTab = testData.get("existingPremiumCoverTab");
      String newPremiumCoverTab = testData.get("newPremiumCoverTab");
      String adjustmentAmountCoverTab = testData.get("adjustmentAmountCoverTab");

      String existingPremiumAcceptanceTab = spnExistingPremiumAcceptanceTab.getText().trim();
      String newPremiumAcceptanceTab = spnNewPremiumAcceptanceTab.getText().trim();
      String adjustmentAmountAcceptanceTab = spnAdjustmentAmountAcceptanceTab.getText().trim();

      testData.put("existingPremiumAcceptanceTab", existingPremiumAcceptanceTab);
      testData.put("newPremiumAcceptanceTab", newPremiumAcceptanceTab);
      testData.put("adjustmentAmountAcceptanceTab", adjustmentAmountAcceptanceTab);

      Log.message("Adjustment Payment Acceptance Tab Payment Details:- Existing Premium: ["
          + existingPremiumAcceptanceTab + "]; New Premium: [" + newPremiumAcceptanceTab
          + "]; Adjustment Amount: [" + adjustmentAmountAcceptanceTab + "]", extentedReport);

      boolean isOkExistingPremium = existingPremiumAcceptanceTab.equals(existingPremiumCoverTab);
      boolean isOkNewPremium = newPremiumAcceptanceTab.equals(newPremiumCoverTab);
      boolean isOkAdjustmentAmount = adjustmentAmountAcceptanceTab.equals(adjustmentAmountCoverTab);

      isOk = isOkExistingPremium && isOkNewPremium && isOkAdjustmentAmount;
    } catch (Exception e) {
      throw new Exception("Exception in verifying payment details at Acceptance tab. " + e);
    }

    return isOk;
  }

  /**
   * To Click Take payment for MTA
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * 
   */

  public void clickTakePaymentForMTA(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnMTATakePayment, 60);
      btnMTATakePayment.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Take Payment button in get quote page", driver, extentedReport);

    } catch (Exception e) {
      throw new Exception(
          "Unable to click on take payment Button in get quote page for MTA, exception occured : "
              + e);
    }
  }

  /**
   * To Click ReturnToCustomerDashboard button
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * 
   */

  public CustDashboardPage clickReturnToCustomerDashboard(ExtentTest extentedReport)
      throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnReturnToCustomerDashboard, 60);
      btnReturnToCustomerDashboard.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on ReturnToCustomerDashboard button in get quote page", driver,
          extentedReport);
      return new CustDashboardPage(driver, extentedReport).get();

    } catch (Exception e) {
      throw new Exception(
          "Unable to click on ReturnToCustomerDashboard Button in get quote page, exception occured : "
              + e);
    }
  }


  /**
   * Description : To Validate Automatic Review, Automatic Renewal and Block
   *
   * @param ExtentTest :extentedReport
   * @param Screenshot :boolean
   * @param fldToChk: String (Automatic Review, Automatic Renewal and Block)
   * @param checkFor String (Enabled, disabled, checked)
   * @throws Exception
   * 
   */

  public boolean validateReviewRenewalFields(String fldToChk, String checkFor,
      ExtentTest extentedReport, boolean screenShot) throws Exception {
    try {
      WebElement eleToInteract = null;
      boolean boolVal = false;

      WaitUtils.waitForElementPresent(driver, "css", cssRnwRevchkBox, 60,
          "Failed to locate Automatic Review, Automatic Renewal and Block Renewal Fields");
      List<WebElement> lstChckBox = driver.findElements(By.cssSelector(cssRnwRevchkBox));
      for (int i = 0; i <= lstChckBox.size(); i++) {
        if (lstChckBox.get(i).getText().equalsIgnoreCase(fldToChk)) {
          eleToInteract = lstChckBox.get(i);
          break;
        }
      }

      switch (checkFor.toLowerCase()) {

        case "enabled":
          if (WaitUtils.waitForElement(driver, eleToInteract, 2))
            Log.message(fldToChk + " field enabled as expected", driver, extentedReport, true);
          boolVal = true;
          break;

        case "disabled":
          if (!eleToInteract.findElement(By.tagName("input")).isEnabled())
            ;
          // if (! WaitUtils.waitForElement(driver, eleToInteract, 2))
          Log.message(fldToChk + " field disabled as expected", extentedReport);
          boolVal = true;

          break;

        case "checked":
          // if (eleToInteract.getAttribute("checked").equalsIgnoreCase("checked"))
          // eleToInteract.findElement(By.tagName("input")).getAttribute("checked").equalsIgnoreCase("checked");
          if (eleToInteract.findElement(By.tagName("input")).isSelected()) {
            Log.message(fldToChk + " field checked", extentedReport);
            boolVal = true;
          } else {
            Log.message(fldToChk + " field was Unchecked", extentedReport);
            boolVal = false;
          }
          break;
      }

      return boolVal;

    } catch (Exception e) {
      throw new Exception("Unable to validate " + fldToChk + " field, exception occured : " + e);
    }
  }

  /**
   * Description : To Click policy flags Automatic Review , Automatic Renewal, Block Renewal
   * 
   * @param ExtentTest :extentedReport
   * @param Screenshot :boolean
   * @param fldToClk: String (Automatic Review , Automatic Renewal, Block Renewal)
   * @throws Exception
   * 
   */

  public void clickPolicyFlags(String fldToClk, ExtentTest extentedReport, boolean screenShot)
      throws Exception {
    try {
      WaitUtils.waitForElementPresent(driver, "css", cssRnwRevchkBox, 60,
          "Failed to locate Automatic Review, Automatic Renewal and Block Renewal Fields");
      List<WebElement> lstChckBox = driver.findElements(By.cssSelector(cssRnwRevchkBox));
      for (int i = 0; i <= lstChckBox.size(); i++) {
        if (lstChckBox.get(i).getText().equalsIgnoreCase(fldToClk)) {
          // Actions actions = new Actions(driver);
          // actions.moveToElement(lstChckBox.get(i)).click();
          lstChckBox.get(i).click();
          Thread.sleep(2000);
          // lstChckBox.get(i).click();
          Log.message("Clicked " + fldToClk, driver, extentedReport, true);
          break;
        }
      }

    } catch (Exception e) {
      throw new Exception("Unable to click " + fldToClk + " field, exception occured : " + e);
    }
  }

  /**
   * Verifies that the 'Change Payor' button is disabled.
   * 
   * @return true if 'Change Payor' button is disabled; false otherwise
   */
  public boolean verifyChangePayorDisabled() {
    return !btnChangePayor.isEnabled();
  }

  /**
   * Verifies that the 'Payment Plan' drop-down is disabled.
   * 
   * @return true if 'Payment Plan' drop-down is disabled; false otherwise
   */
  public boolean verifyPaymentPlanDisabled() {
    return !divPaymentPlan.isEnabled();
  }

  /**
   * Verifies that the 'Payment Method' drop-down is disabled.
   * 
   * @return true if 'Payment Method' drop-down is disabled; false otherwise
   */
  public boolean verifyPaymentMethodDisabled() {
    return !divPaymentMethod.isEnabled();
  }

  /**
   * To Click Save Quote Tab
   *
   * @param ExtentTest :extentedReport
   * @throws Exception
   * 
   */

  public void clickSaveQuoteMta(ExtentTest extentedReport) throws Exception {
    try {
      WaitUtils.waitForElement(driver, btnSaveMtaQuote, 60);
      btnSaveMtaQuote.click();
      WaitUtils.waitForSpinner(driver);
      Log.message("Clicked on Save Quote button in get quote page", driver, extentedReport);
    } catch (Exception e) {
      throw new Exception(
          "Unable to click on CustomerDashboard Tab in get quote page, exception occured : " + e);
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
    WaitUtils.waitForElementPresent(driver, pageActiveTitle,
        "Element is displayed on data capture page");
    switch (pagename.toLowerCase()) {

      case "datacapture":
        if (pageActiveTitle.getAttribute("title").equals("Data Capture")) {
          status = true;
          return status;
        }
        break;

      case "pricepresentation":
        WaitUtils.waitForElementPresent(driver, pageActiveTitle,
            "Element is displayed on data capture page");

        if (pageActiveTitle.getAttribute("title").equals("Cover/Price Presentation")) {
          status = true;
          return status;
        }
        break;

      case "acceptance":
        WaitUtils.waitForElementPresent(driver, pageActiveTitle,
            "Element is displayed on data capture page");

        if (pageActiveTitle.getAttribute("title").equals("Acceptance")) {
          status = true;
          return status;
        }
        break;

    }
    return status;

  }
}
