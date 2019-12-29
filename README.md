# Nefrapp

TODO:
- Nella pagina di invio messaggi bisogna disporre che quando la si lascia senza inviare l'allegato il messaggio temporaneo caricato deve essere cancellato.

Glossario delle nuove variabili di sessione:
- ${isPaziente}, ${isMedico}, ${isAmministratore} sono boolean settati a true quando un utente della corrispondente categoria ha effettuato l'accesso al sito.
- ${accessDone} è un boolean settato a true quando un utente qualsiasi ha effettuato l'accesso al sito.
- ${utente} è un bean Utente contenente le informazioni che modellano l'utente attualmente in sessione.

Non è necessario e ripeto NON È NECESSARIO dichiararare tali variabili nelle jsp, in quanto sono dichiarate in header e sidebar, e tutte le altre pagine le includono. Usatele soltanto, i nomi dovrebbero essere abbastanza intuitivi da ricordarli e non doverli ripescare dalla sessione.
