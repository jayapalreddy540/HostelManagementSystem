const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();


exports.sendNotification = functions.database.ref('/outing/{user_id}/{outing_id}').onWrite((change, context) => {

    /*
     * You can store values as variables from the 'database.ref'
     * Just like here, I've done for 'user_id' and 'notification'
     */

    const user_id = context.params.user_id;
    const outing_id = context.params.outing_id;

    console.log("we have a notificaion to send to : ", user_id);

    /*
     * Stops proceeding to the rest of the function if the entry is deleted from database.
     * If you want to work with what should happen when an entry is deleted, you can replace the
     * line from "return console.log.... "
     */

    if (!change.after.val()) {
        return console.log('A notification has been deleted from the database : ', outing_id);
    }

    /*
     *'touser' query retreives the ID of the warden the notification to be sent
     */


    const toUser = admin.database().ref(`/users/students/${user_id}`).once('value');
    return toUser.then(fromUserResult => {
        const to_user_id = fromUserResult.val().caretaker;
        console.log('outing requested : ', user_id);

        /*
         * Then we run two queries at a time using Firebase 'Promise'.
         * One to get the name of the user who requested outing
         * another one to get the devicetoken to the device we want to send notification to
         */


        const userQuery = admin.database().ref(`/users/students/${user_id}/name`).once('value');
        const deviceToken = admin.database().ref(`/users/wardens/${to_user_id}/device_token`).once('value');

        return Promise.all([userQuery, deviceToken])
            .then(result => {
                const userName = result[0].val();
                const token_id = result[1].val();
                /*
                 * We are creating a 'payload' to create a notification to be sent.
                 */

                const payload = {
                    notification: {
                        title: " Outing Request",
                        body: `${userName} requested an outing`,
                        icon: "@mipmap/app_icon",
                        click_action: "tk.codme.hostelmanagementsystem_TARGET_NOTIFICATION",
                    },
                    data: {
                        user_id: from_user_id
                    }
                };

                /*
                 * Then using admin.messaging() we are sending the payload notification to the token_id of
                 * the device we retreived.
                 */
                return admin.messaging().sendToDevice(token_id, payload)
                    .then(response => {
                        return console.log('This is the notification feature ', response);
                    });
            });
    });
});


