# Nefrapp

Glossario delle nuove variabili di sessione:
- ${isPaziente}, ${isMedico}, ${isAmministratore} sono boolean settati a true quando la corrispondente categoria d'utente ha effettuato l'accesso al sito.
- ${accessDone} è un boolean settato a true quando un utente qualsiasi ha effettuato l'accesso al sito.
- ${utente} è un bean Utente contenente le informazioni che modellano l'utente attualmente in sessione.

Non è necessario e ripeto NON È NECESSARIO dichiararare tali variabili nelle jsp, in quanto sono dichiarate in header e sidebar, e tutte le altre pagine le includono. 
