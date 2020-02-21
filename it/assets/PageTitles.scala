package assets

object PageTitles {
  val consentingCompany = "Is this a consenting company?"
  val aboutAddingUKCompanies = "In this section you will need to tell us about eligible UK companies in the group"
  val partnershipSAUTR: String => String = name => s"Enter $name’${if(name.last.toLower != 's')'s'} Self Assessment Unique Taxpayer Reference"
  val isUkPartnership: String => String = name => s"Is $name a UK Partnership?"
  val partnershipName = "Enter the name of the partnership"
  val investorRatioMethod = "Which ratio do you want to elect for this investor group?"
  val investorGroupName = "Enter the name of the investor group"
  val addInvestorGroup = "Do you want to add an investor group?"
  val otherInvestorGroupElections = "Which other elections apply to this investor group?"
  val groupEBITDA = "Enter the group-EBITDA"
  val interestAllowanceConsolidatedPshipElection = "Do you want to make an interest allowance election for consolidated partnerships?"
  val electedInterestAllowanceConsolidatedPshipBefore = "Has the group made an interest allowance election for consolidated partnerships in any previous returns?"
  val interestAllowanceNonConsolidatedInvestmentsElection = "Do you want to make an interest allowance election for non-consolidated investments?"
  val groupRatioPercentage = "Enter the group ratio percentage"
  val interestAllowanceAlternativeCalcElection = "Do you want to make an interest allowance (alternative calculation) election for this return?"
  val electedInterestAllowanceAlternativeCalcBefore = "Has the group made an interest allowance (alternative calculation) election in any previous returns?"
  val groupEBITDAChargeableGainsElection = "Do you want to make a group-EBITDA (chargeable gains) election for this return?"
  val electedGroupEBITDABefore = "Has the group made a group-EBITDA (chargeable gains) election in any previous returns?"
  val groupRatioBlendedElection = "Are you making a blended group ratio election?"
  val enterQNGIE = "Enter the qualifying net group-interest expense (QNGIE)"
  val checkAnswersGroupStructure = "Check parent company details"
  val enterANGIE = "Enter the adjusted net group-interest expense (ANGIE)"
  val groupRatioElection = "Are you making a group ratio election?"
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
  val continueSavedReturn = "Do you want to start a new return or continue working on a saved return?"
  val deemedParent = "Does the group have a ’deemed’ parent company?"
  val parentCompanyName = "Enter the name of the parent company"
  val parentCRN = "Parent Company Registration Number"
  val payTaxInUk: String => String = name => s"Does $name have a Unique Taxpayer Reference?"
  val reportingCompanySameAsParent: String => String = name => s"Is $name also the parent company?"
  val parentCompanySAUTR = "Parent company Self Assessment Unique Taxpayer Reference"
  val parentCompanyCTUTR = "Parent Company Corporation Unique Taxpayer Reference"
  val registeredCompaniesHouse = "Is the parent company registered with companies house?"
  val savedReturn = "Your return has been saved"
  val registeredForTaxInAnotherCountry: String => String = name => s"Is $name registered for tax in another country?"
  val limitedLiabilityPartnership: String => String = name => s"Is $name a Limited Liability Partnership?"
  val localRegistrationNumber: String => String = name => s"Enter $name’${if(name.last.toLower != 's')'s'} local Registration Number"
  val countryOfIncorporation: String => String = name => s"Where is $name registered for tax?"
  val checkAnswersElections = "Check your answers for this section"
  val companyTaxEBITDA = "Enter company’s Tax-EBITDA"


}
