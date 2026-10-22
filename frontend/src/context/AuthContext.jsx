import React, { createContext, useContext, useState, useEffect } from 'react';
import axiosClient from '../api/axiosClient';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  // Initialize auth state from localStorage on first render
  useEffect(() => {
    try {
      const storedToken = localStorage.getItem('token');
      const storedUser = localStorage.getItem('user');

      if (storedToken && storedUser) {
        setToken(storedToken);
        setUser(JSON.parse(storedUser));
      }
    } catch (err) {
      console.error('Failed to restore authentication session:', err);
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    } finally {
      setLoading(false);
    }
  }, []);

  // Login handler
  const login = async (email, password) => {
    try {
      const response = await axiosClient.post('/auth/login', { email, password });
      const { token: jwtToken, user: userData } = response.data;

      localStorage.setItem('token', jwtToken);
      localStorage.setItem('user', JSON.stringify(userData));

      setToken(jwtToken);
      setUser(userData);
      return { success: true };
    } catch (error) {
      // For developer convenience when testing frontend before backend DB is seeded:
      // If demo credentials match, offer instant local session
      if (email === 'demo.developer@example.com' && password === 'Password123!') {
        const demoUser = {
          id: 1,
          email: 'demo.developer@example.com',
          fullName: 'Alex Morgan',
          targetRole: 'Java Full Stack Developer',
          role: 'ROLE_USER',
        };
        const demoToken = 'demo-jwt-token-alex-morgan-session';
        localStorage.setItem('token', demoToken);
        localStorage.setItem('user', JSON.stringify(demoUser));
        setToken(demoToken);
        setUser(demoUser);
        return { success: true };
      }

      const errorMessage =
        error.response?.data?.message ||
        error.response?.data?.error ||
        'Authentication failed. Please check your credentials or backend server.';
      return { success: false, error: errorMessage };
    }
  };

  // Register handler
  const register = async (formData) => {
    try {
      const response = await axiosClient.post('/auth/register', formData);
      const { token: jwtToken, user: userData } = response.data;

      localStorage.setItem('token', jwtToken);
      localStorage.setItem('user', JSON.stringify(userData));

      setToken(jwtToken);
      setUser(userData);
      return { success: true };
    } catch (error) {
      const errorMessage =
        error.response?.data?.message ||
        error.response?.data?.error ||
        'Registration failed. Please try again.';
      return { success: false, error: errorMessage };
    }
  };

  // Logout handler
  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  };

  // Update profile handler
  const updateProfile = (updatedUserData) => {
    const updated = { ...user, ...updatedUserData };
    localStorage.setItem('user', JSON.stringify(updated));
    setUser(updated);
  };

  const value = {
    user,
    token,
    loading,
    isAuthenticated: !!token && !!user,
    login,
    register,
    logout,
    updateProfile,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
