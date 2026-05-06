import {useEffect, useState} from 'react';
import {checkLoginStatus} from './api/auth';
import DashboardPage from './pages/DashboardPage/DashboardPage';
import LoginPage from './pages/LoginPage/LoginPage';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isCheckingLogin, setIsCheckingLogin] = useState(true);

    useEffect(() => {
        let isMounted = true;

        const restoreLoginStatus = async () => {
            try {
                const loggedIn = await checkLoginStatus();

                if (isMounted) {
                    setIsLoggedIn(loggedIn);
                }
            } catch (error) {
                console.error(error);

                if (isMounted) {
                    setIsLoggedIn(false);
                }
            } finally {
                if (isMounted) {
                    setIsCheckingLogin(false);
                }
            }
        };

        void restoreLoginStatus();

        return () => {
            isMounted = false;
        };
    }, []);

    if (isCheckingLogin) {
        return <main className="login-page">Loading...</main>;
    }

    if (isLoggedIn) {
        return <DashboardPage onLogoutSuccess={() => setIsLoggedIn(false)}/>;
    }

    return <LoginPage onLoginSuccess={() => setIsLoggedIn(true)}/>;
}

export default App;
