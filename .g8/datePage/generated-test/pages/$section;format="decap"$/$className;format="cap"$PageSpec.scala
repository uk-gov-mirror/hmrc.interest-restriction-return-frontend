package pages.$section;format="decap"$

import java.time.LocalDate

import org.scalacheck.Arbitrary
import pages.behaviours.PageBehaviours

class $className;format="cap"$PageSpec extends PageBehaviours {

  "$className;format="cap"$Page" must {

    implicit lazy val arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary {
      datesBetween(LocalDate.of(1900, 1, 1), LocalDate.of(2100, 1, 1))
    }

    beRetrievable[LocalDate]($className;format="cap"$Page)

    beSettable[LocalDate]($className;format="cap"$Page)

    beRemovable[LocalDate]($className;format="cap"$Page)
  }
}
