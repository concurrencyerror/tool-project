import {useState, type FormEvent} from 'react';
import {login} from '../../api/auth';
import './LoginPage.css';

type LoginPageProps = {
    onLoginSuccess: () => void;
};

function LoginPage({onLoginSuccess}: LoginPageProps) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [loginMessage, setLoginMessage] = useState('');

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setLoginMessage('');
        setIsSubmitting(true);

        try {
            await login(username.trim(), password);
            onLoginSuccess();
        } catch (error) {
            console.error(error);
            setLoginMessage(error instanceof Error ? error.message : '登录失败，请稍后再试');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <main className="login-page">
            <h1 className="brand">工具管理台</h1>

            <section className="login-shell" aria-label="登录">
                <div className="login-panel">
                    <h2 className="login-title">登录</h2>

                    <form className="login-form" onSubmit={handleSubmit}>
                        <label className="field">
                            <span>用户名</span>
                            <input
                                type="text"
                                name="username"
                                placeholder="请输入用户名"
                                autoComplete="username"
                                value={username}
                                onChange={(event) => setUsername(event.target.value)}
                                required
                            />
                        </label>

                        <label className="field">
                            <span>密码</span>
                            <input
                                type="password"
                                name="password"
                                placeholder="请输入密码"
                                autoComplete="current-password"
                                value={password}
                                onChange={(event) => setPassword(event.target.value)}
                                required
                            />
                        </label>

                        <button className="login-button" type="submit" disabled={isSubmitting}>
                            {isSubmitting ? '登录中...' : '登录'}
                        </button>

                        {loginMessage ? <p className="submit-message error">{loginMessage}</p> : null}
                    </form>
                </div>
            </section>
        </main>
    );
}

export default LoginPage;
