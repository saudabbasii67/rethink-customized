package com.celzero.bravedns.ui

class Strings {
    val strings = listOf(

        "Regularly update your software and operating systems to ensure you have the latest security patches, and use strong, unique passwords for each account to minimize the risk of unauthorized access.",
        "Enable two-factor authentication (2FA) whenever possible to add an extra layer of security to your online accounts.",
        "Be cautious of unsolicited emails and avoid clicking on links or downloading attachments from unknown sources.",
        "Regularly back up your important data and files to a secure external location to mitigate the impact of potential cyberattacks.",
        "Use a reputable antivirus and anti-malware software to protect your devices from viruses, ransomware, and other malicious software.",
        "Educate yourself about phishing scams and social engineering techniques to recognize and avoid falling for fraudulent schemes.",
        "Secure your home Wi-Fi network with a strong password and encryption to prevent unauthorized access to your internet connection.",
        "Limit the amount of personal information you share on social media platforms to reduce the risk of identity theft and targeted attacks.",
        "Implement a firewall to control incoming and outgoing network traffic, enhancing your system's defense against cyber threats.",
        "Implement a firewall to control incoming and outgoing network traffic, enhancing your system's defense against cyber threats.",
        "Stay informed about the latest cybersecurity trends and news to understand emerging threats and best practices for protection.",
        "When using public Wi-Fi, connect through a virtual private network (VPN) to encrypt your internet connection and safeguard your data.",
        "Keep an eye out for software vulnerabilities and apply updates promptly to prevent exploitation by cybercriminals.",
        "Secure physical access to your devices by using strong passwords, PINs, or biometric authentication methods.",
        "Implement access controls and user permissions to ensure that only authorized individuals can access sensitive data and systems.",
        "Regularly audit and review your privacy settings on social media and online accounts to maintain control over your personal information.",
        "Be cautious when downloading and installing applications, especially from third-party sources. Stick to official app stores when possible.",
        "Consider using a password manager to generate and store complex passwords securely for all your accounts.",
        "Create a separate email address for online shopping and promotional offers to reduce the exposure of your primary email to potential threats.",
        "Develop a cybersecurity incident response plan to know how to react in case of a breach, minimizing damage and downtime.",
        "Encrypt sensitive data before storing it in the cloud or transmitting it online to prevent unauthorized access to confidential information.",
        "Only download apps from the official Google Play Store to minimize the risk of installing malicious software.",
        "Check and understand the permissions an app requests before granting access to your device's features and data.",
        "Regularly update your Android operating system to ensure you have the latest security patches.",
        "Set up a strong PIN, password, pattern, or biometric lock screen to prevent unauthorized access to your device.",
        "Activate the \"Find My Device\" feature to help locate, lock, or erase your device remotely in case it's lost or stolen.",
        "Be cautious when clicking on links in text messages or answering calls from unknown numbers to avoid phishing scams.",
        "Install a reputable mobile security app to scan for and protect against malware and other threats.",
        "Back up your Android device to a secure location, such as Google Drive, to ensure your important data is safe.",
        "Use strong passwords and WPA3 encryption for your Wi-Fi networks to prevent unauthorized access.",
        "Avoid accessing sensitive information or making transactions over public Wi-Fi networks to minimize the risk of interception.",
        "Don't trust unexpected emails, messages, or pop-ups asking for personal information or urgent action.",
        "Create passwords with a mix of letters, numbers, and symbols. Avoid using easily guessable information like birthdays or names.",
        "Use different passwords for different accounts to prevent a single breach from affecting multiple accounts.",
        "Turn on 2FA whenever possible to add an extra layer of security to your accounts.",
        "Be cautious about sharing personal details on social media or with unknown contacts.",
        "Keep your apps updated to get the latest security patches and features.",
        "Don't click on links or download attachments from unfamiliar sources, even if they seem legitimate.",
        "Change your Wi-Fi password from the default and enable WPA3 encryption for added security.",
        "Set up PINs, passwords, or patterns to lock your devices and prevent unauthorized access.",
        "Look for \"https://\" and a padlock symbol in the address bar before entering sensitive information on websites.",
        "Stay informed about common scams and tactics used by cybercriminals to recognize and avoid them.",
        "Avoid using public computers for sensitive tasks like online banking, as they might be compromised.",
        "Shred paper documents containing personal information before disposing of them.",
        "Be mindful about sharing too much personal information on social media, which can be exploited by attackers.",
        "Use a screen protector and a case to prevent physical damage and access to your device.",
        "Change the default router login credentials and use a strong, unique passphrase for Wi-Fi access.",
        "Regularly back up your data to an external hard drive or a secure cloud service.",
        "Avoid accessing sensitive accounts or making financial transactions on public Wi-Fi networks.",
        "Before providing any information or funds, verify the identity of callers or senders through trusted channels.",
        "If something seems suspicious or too good to be true, it probably is. Trust your gut and proceed with caution.",
        "Keep up with the latest cybersecurity news and trends to stay aware of potential threats and how to protect yourself online."

        )

    fun getRandomString(): String {
        // Get a random index from the list of strings.
        val randomIndex = (0 until strings.size).random()

        // Return the string at the random index.
        return strings[randomIndex]
    }
}