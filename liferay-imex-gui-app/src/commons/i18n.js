import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'

// the translations
// (tip move them in a JSON file and import them,
// or even better, manage them via a UI: https://react.i18next.com/guides/multiple-translation-files#manage-your-translations-with-a-management-gui)
const resources = {
  en: {
    translation: {
      'import-process-description': 'Import process read datas from filesystem and write it to Liferay',
      'export-process-description': 'Export process read datas from Liferay and write it to filesystem',
      'unsigned-alert-message-title': 'Unknown user',
      'unsigned-alert-message-description': 'You need to sign in to see this content',
      'technical-alert-message-description': 'A technical error occur',
      'validation-error-label': 'Is there something wrong',
      'at-least-one-exporter-alert-message': 'No selected exporter. At least one exporter need to be selected.',
      'main-title': 'IMEX administration console',
      'main-description': 'Use available exporter or importers to load or extract datas from Liferay.',
      'title-exporters': 'List of registered exporters',
      'title-importers': 'List of registered importers',
      'label-priority': 'Priority',
      'label-ranking': 'Ranking',
      'label-supported-profiles-ids': 'Profiles',
      'button-label-run': 'Run',
      'button-import': 'Import TO Liferay',
      'button-export': 'Export FROM Liferay',
      'empty-datas-message': 'No elements to display'
    }
  },
  fr: {
    translation: {
      'import-process-description': 'Le processus d\'import utilise les données stockées sur le filesystem pour les écrire dans liferay',
      'export-process-description': 'Le processus d\'export utilise extrait les données de Liferay pour les écrire dans le filesystem',
      'unsigned-alert-message-title': 'Utilisateur inconnu',
      'unsigned-alert-message-description': 'Vous devez vous connecter pour accèder à ce contenu',
      'technical-alert-message-description': 'Une erreur technique c\'est produite',
      'validation-error-label': 'Quelque chose ne va pas',
      'at-least-one-exporter-alert-message': 'Vous devez selectionner au moins un exporter avant de lancer cette action',
      'main-title': 'Console d\'administration IMEX',
      'main-description': 'Utiliser les processus d\'import / export disponibles pour charger ou récupérer des données de votre instance Liferay.',
      'title-exporters': 'Liste des exporteurs disponibles',
      'title-importers': 'Liste des importeurs disponibles',
      'label-priority': 'Priorité',
      'label-ranking': 'Ranking',
      'label-supported-profiles-ids': 'Profiles',
      'button-label-run': 'Lancer',
      'button-import': 'Importer DANS liferay',
      'button-export': 'Exporter DE Liferay',
      'empty-datas-message': 'Il n\'y a pas d\'éléments à afficher'
    }
  }
}

const DEFAULT_LANGUAGE = 'en'

export function getLanguage () {
  if (process.env.NODE_ENV === 'development') {
    return DEFAULT_LANGUAGE
  }
  const languageId = Liferay().ThemeDisplay.getLanguageId()
  let language = DEFAULT_LANGUAGE
  if (languageId) {
    // Transform en_US to en
    language = languageId.substring(0, 2)
  }
  return language
}

export function Liferay () {
  return window.Liferay
}

i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .init({
    resources,
    lng: getLanguage(), // language to use, more information here: https://www.i18next.com/overview/configuration-options#languages-namespaces-resources
    // you can use the i18n.changeLanguage function to change the language manually: https://www.i18next.com/overview/api#changelanguage
    // if you're using a language detector, do not define the lng option

    interpolation: {
      escapeValue: false // react already safes from xss
    }
  })

i18n.changeLanguage(getLanguage(), (err, t) => {
  if (err) return console.log('Something went wrong loading', err)
})

export default i18n
