package assets

object PageTitles {

  val checkAnswersReportingCompany = "Check your answers - About the reporting company"
  val reportingCompanyCRN = "Company Registration Number (CRN)"
  val reportingCompanyCTUTR = "UK Tax reference"
  val reportingCompanyName = "Reporting company name"
  val groupInterestAllowance = "What is the group interest allowance for the period?"
  val groupInterestCapacity = "What is the group interest capacity for the period?"
  val groupSubjectToReactivations = "Is the group subject to reactivations?"
  val groupSubjectToRestrictions = "Is the group subject to restrictions?"
  val infrastructureCompanyElection = "Has the group made the Infrastructure company election?"
  val interestAllowanceBroughtForward = "What is the group interest allowance brought forward?"
  val interestReactivationsCap = "What is the group reactivation cap?"
  val returnContainEstimates = "Does the return contain estimates?"
  val revisingReturn = "Are you revising a return you have already submitted?"
  val agentActingOnBehalfOfCompany = "Are you an agent acting on behalf of a company?"
  val agentName = "Agent name"
  val fullOrAbbreviatedReturn = "Do you want to submit a full or abbreviated return?"
  val reportingCompanyAppointed = "Has the reporting company been appointed"
  val reportingCompanyRequired = "A reporting company is required"
  val confirmation = "Return submitted"
  val deemedParent = "Does the group have a ’deemed’ parent company?"
  val parentCompanyName = "Enter the name of the parent company"
  val parentCRN = "Parent Company Registration Number"
  val payTaxInUk: String => String = name => s"Does $name have a Unique Taxpayer Reference?"
  val reportingCompanySameAsParent: String => String = name => s"Is $name also the parent company?"
  val parentCompanySAUTR = "Parent company Self Assessment Unique Tax Payer Reference"
  val parentCompanyCTUTR = "Parent Company Corporation Unique Tax Payer Reference"
  val registeredCompaniesHouse = "Is the parent company registered with companies house?"
  val limitedLiabilityPartnership: String => String = name => s"Is $name a Limited Liability Partnership?"


}
