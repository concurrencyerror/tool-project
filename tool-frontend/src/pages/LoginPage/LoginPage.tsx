import {useState, type FormEvent} from 'react';
import {login} from '../../api/auth';
import './LoginPage.css';

type LoginPageProps = {
    onLoginSuccess: () => void;
};

function LoginPage({onLoginSuccess}: LoginPageProps) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [loginMessage, setLoginMessage] = useState('');

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setLoginMessage('');
        setIsSubmitting(true);

        try {
            await login(email, password);
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
                            <span>邮箱</span>
                            <input
                                type="email"
                                name="email"
                                placeholder="请输入邮箱"
                                autoComplete="email"
                                value={email}
                                onChange={(event) => setEmail(event.target.value)}
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
