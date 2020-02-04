package pages

import models.$className;format="cap"$
import pages.behaviours.PageBehaviours

class $className;format="cap"$Spec extends PageBehaviours {

  "$className;format="cap"$Page" must {

    beRetrievable[$className;format="cap"$]($className;format="cap"$Page)

    beSettable[$className;format="cap"$]($className;format="cap"$Page)

    beRemovable[$className;format="cap"$]($className;format="cap"$Page)
  }
}
