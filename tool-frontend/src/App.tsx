import {useState} from 'react';
import DashboardPage from './pages/DashboardPage/DashboardPage';
import LoginPage from './pages/LoginPage/LoginPage';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    if (isLoggedIn) {
        return <DashboardPage onLogoutSuccess={() => setIsLoggedIn(false)}/>;
    }

    return <LoginPage onLoginSuccess={() => setIsLoggedIn(true)}/>;
}

export default App;
