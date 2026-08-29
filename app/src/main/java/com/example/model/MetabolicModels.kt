package com.example.model

enum class Gender(val label: String) {
  MALE("Masculino"),
  FEMALE("Feminino")
}

enum class ActivityLevel(
  val label: String,
  val factor: Double,
  val description: String
) {
  SEDENTARY(
    label = "Sedentário",
    factor = 1.2,
    description = "Pouco ou nenhum exercício"
  ),
  LIGHT(
    label = "Leve",
    factor = 1.375,
    description = "Exercício leve 1 a 3 dias/semana"
  ),
  MODERATE(
    label = "Moderado",
    factor = 1.55,
    description = "Exercício moderado 3 a 5 dias/semana"
  ),
  INTENSE(
    label = "Intenso",
    factor = 1.725,
    description = "Exercício pesado 6 a 7 dias/semana"
  ),
  VERY_INTENSE(
    label = "Muito Intenso",
    factor = 1.9,
    description = "Exercício pesado diário ou trabalho físico"
  )
}

data class MetabolicCalculationResult(
  val bmr: Double,              // Taxa Metabólica Basal (TMB) em kcal/dia
  val dailyCaloricExpenditure: Double, // Gasto Calórico Diário Total em kcal/dia
  val age: Int,
  val weightKg: Double,
  val heightCm: Double,
  val gender: Gender,
  val activityLevel: ActivityLevel,
  val dailyWaterRequirementMl: Int = (weightKg * 35).toInt(), // Quantidade de água diária necessária (35ml/kg)
  val userName: String = "Meu Perfil",
  val avatarId: String = "avatar_1",
  val bannerId: String = "banner_1",
  val customAvatarUri: String? = null,
  val customBannerUri: String? = null
) {
  val dailyWaterRequirementLiters: Double
    get() = dailyWaterRequirementMl / 1000.0
}

object MetabolicCalculator {
  /**
   * Calcula a Taxa Metabólica Basal (TMB) segundo a fórmula de Mifflin-St Jeor:
   * Homens: (10 × peso) + (6.25 × altura) − (5 × idade) + 5
   * Mulheres: (10 × peso) + (6.25 × altura) − (5 × idade) − 161
   */
  fun calculateBmr(
    weightKg: Double,
    heightCm: Double,
    ageYears: Int,
    gender: Gender
  ): Double {
    val base = (10.0 * weightKg) + (6.25 * heightCm) - (5.0 * ageYears)
    return if (gender == Gender.MALE) {
      base + 5.0
    } else {
      base - 161.0
    }
  }

  /**
   * Calcula o Gasto Calórico Diário Total multiplicando a TMB pelo fator de atividade física.
   */
  fun calculateTotalExpenditure(
    bmr: Double,
    activityLevel: ActivityLevel
  ): Double {
    return bmr * activityLevel.factor
  }

  /**
   * Calcula a quantidade de água recomendada por dia em mililitros (ml)
   * Base científica: 35 ml por kg de peso corporal (com pequeno acréscimo proporcional para atividades intensas)
   */
  fun calculateWaterIntake(weightKg: Double, activityLevel: ActivityLevel): Int {
    val mlPerKg = when (activityLevel) {
      ActivityLevel.SEDENTARY -> 35.0
      ActivityLevel.LIGHT -> 35.0
      ActivityLevel.MODERATE -> 38.0
      ActivityLevel.INTENSE -> 40.0
      ActivityLevel.VERY_INTENSE -> 45.0
    }
    return (weightKg * mlPerKg).toInt()
  }

  fun calculate(
    weightKg: Double,
    heightCm: Double,
    ageYears: Int,
    gender: Gender,
    activityLevel: ActivityLevel,
    userName: String = "Meu Perfil",
    avatarId: String = "avatar_1",
    bannerId: String = "banner_1",
    customAvatarUri: String? = null,
    customBannerUri: String? = null
  ): MetabolicCalculationResult {
    val bmr = calculateBmr(weightKg, heightCm, ageYears, gender)
    val totalExpenditure = calculateTotalExpenditure(bmr, activityLevel)
    val waterMl = calculateWaterIntake(weightKg, activityLevel)
    return MetabolicCalculationResult(
      bmr = bmr,
      dailyCaloricExpenditure = totalExpenditure,
      age = ageYears,
      weightKg = weightKg,
      heightCm = heightCm,
      gender = gender,
      activityLevel = activityLevel,
      dailyWaterRequirementMl = waterMl,
      userName = userName,
      avatarId = avatarId,
      bannerId = bannerId,
      customAvatarUri = customAvatarUri,
      customBannerUri = customBannerUri
    )
  }
}
