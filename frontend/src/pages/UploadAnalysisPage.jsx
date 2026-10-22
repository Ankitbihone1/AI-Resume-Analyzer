import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosClient from '../api/axiosClient';
import {
  Upload,
  FileText,
  X,
  AlertCircle,
  Sparkles,
  CheckCircle2,
  Loader2,
  ShieldAlert,
  ArrowRight,
  Briefcase,
} from 'lucide-react';

export default function UploadAnalysisPage() {
  const navigate = useNavigate();
  const fileInputRef = useRef(null);

  const [file, setFile] = useState(null);
  const [targetRole, setTargetRole] = useState('Junior Java Full Stack Developer');
  const [jobDescription, setJobDescription] = useState(
    'We are seeking an enthusiastic Junior Java Full Stack Developer proficient in Java 17, Spring Boot, React.js, RESTful APIs, MySQL, and Docker. Experience with automated testing (JUnit/Mockito) and Git is required.'
  );

  const [dragActive, setDragActive] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // File drag handlers
  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const validateAndSetFile = (selectedFile) => {
    setError('');
    if (!selectedFile) return;

    if (selectedFile.type !== 'application/pdf' && !selectedFile.name.toLowerCase().endsWith('.pdf')) {
      setError('Only PDF documents (.pdf) are supported. Please select a valid resume PDF.');
      return;
    }

    // 10 MB limit check
    if (selectedFile.size > 10 * 1024 * 1024) {
      setError('File size exceeds the 10 MB limit. Please upload a smaller PDF resume.');
      return;
    }

    setFile(selectedFile);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      validateAndSetFile(e.dataTransfer.files[0]);
    }
  };

  const handleFileInputChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      validateAndSetFile(e.target.files[0]);
    }
  };

  const handleRemoveFile = () => {
    setFile(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!file) {
      setError('Please select or upload a resume PDF document.');
      return;
    }

    if (!targetRole.trim()) {
      setError('Please enter a target job title or role.');
      return;
    }

    if (!jobDescription.trim() || jobDescription.trim().length < 20) {
      setError('Please provide a meaningful job description (at least 20 characters).');
      return;
    }

    try {
      setLoading(true);
      const formData = new FormData();
      formData.append('file', file);
      formData.append('targetRole', targetRole.trim());
      formData.append('jobDescription', jobDescription.trim());

      const response = await axiosClient.post('/analyses', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      // Navigate to results page with response ID
      if (response.data && response.data.id) {
        navigate(`/results/${response.data.id}`);
      } else {
        navigate('/results/1');
      }
    } catch (err) {
      console.warn('Backend API endpoint will be fully connected in Stage 3-5:', err);
      // Fallback for UI demonstration in Stage 2
      navigate('/results/1');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Page Title */}
      <div>
        <h2 className="text-2xl font-bold text-slate-900 tracking-tight">Evaluate New Resume</h2>
        <p className="text-sm text-slate-500">
          Upload your candidate resume in PDF and pair it with a job description for instant ATS matching.
        </p>
      </div>

      {error && (
        <div className="p-4 rounded-xl bg-rose-50 border border-rose-200 flex items-start space-x-3 text-sm text-rose-800">
          <AlertCircle className="w-5 h-5 text-rose-600 shrink-0 mt-0.5" />
          <span>{error}</span>
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Step 1: File Dropzone */}
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-base font-bold text-slate-900 flex items-center space-x-2">
              <span className="w-6 h-6 rounded-full bg-brand-600 text-white flex items-center justify-center text-xs">
                1
              </span>
              <span>Upload Resume (PDF format)</span>
            </h3>
            <span className="text-xs text-slate-400 font-medium">Max 10 MB</span>
          </div>

          {!file ? (
            <div
              onDragEnter={handleDrag}
              onDragLeave={handleDrag}
              onDragOver={handleDrag}
              onDrop={handleDrop}
              onClick={() => fileInputRef.current?.click()}
              className={`border-2 border-dashed rounded-xl p-8 text-center cursor-pointer transition-all ${
                dragActive
                  ? 'border-brand-500 bg-brand-50/50 scale-[1.01]'
                  : 'border-slate-300 hover:border-brand-400 hover:bg-slate-50/50'
              }`}
            >
              <input
                ref={fileInputRef}
                type="file"
                accept=".pdf,application/pdf"
                onChange={handleFileInputChange}
                className="hidden"
              />
              <div className="w-12 h-12 rounded-xl bg-brand-50 text-brand-600 flex items-center justify-center mx-auto mb-3">
                <Upload className="w-6 h-6" />
              </div>
              <p className="text-sm font-semibold text-slate-800 mb-1">
                Click to browse or drag and drop your PDF resume here
              </p>
              <p className="text-xs text-slate-500">
                Supports readable text PDFs. Ensure document is not an image-only scan.
              </p>
            </div>
          ) : (
            <div className="p-4 rounded-xl bg-brand-50/60 border border-brand-200 flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 rounded-lg bg-brand-600 text-white flex items-center justify-center shrink-0 shadow-sm">
                  <FileText className="w-5 h-5" />
                </div>
                <div>
                  <p className="text-sm font-bold text-slate-900 truncate max-w-[280px] sm:max-w-md">
                    {file.name}
                  </p>
                  <p className="text-xs text-slate-500">
                    {(file.size / 1024).toFixed(1)} KB • PDF Document Ready
                  </p>
                </div>
              </div>

              <button
                type="button"
                onClick={handleRemoveFile}
                className="p-1.5 rounded-lg text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition"
                title="Remove Selected File"
              >
                <X className="w-5 h-5" />
              </button>
            </div>
          )}
        </div>

        {/* Step 2: Target Role & Job Description */}
        <div className="card space-y-4">
          <h3 className="text-base font-bold text-slate-900 flex items-center space-x-2">
            <span className="w-6 h-6 rounded-full bg-brand-600 text-white flex items-center justify-center text-xs">
              2
            </span>
            <span>Job Role &amp; Requirements</span>
          </h3>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              Target Job Title / Position
            </label>
            <div className="relative">
              <Briefcase className="w-4 h-4 text-slate-400 absolute left-3.5 top-3" />
              <input
                type="text"
                value={targetRole}
                onChange={(e) => setTargetRole(e.target.value)}
                placeholder="e.g. Senior Backend Engineer / Java Developer"
                className="input-field pl-10"
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              Paste Job Description or Requirements
            </label>
            <textarea
              rows={6}
              value={jobDescription}
              onChange={(e) => setJobDescription(e.target.value)}
              placeholder="Paste job posting text, required skills, tools, and years of experience here..."
              className="input-field py-2.5 font-mono text-xs leading-relaxed"
              required
            />
          </div>
        </div>

        {/* Action Button & Disclaimer */}
        <div className="space-y-3">
          <button
            type="submit"
            disabled={loading}
            className="btn-primary w-full py-3.5 text-base space-x-2 shadow-lg shadow-brand-500/20"
          >
            {loading ? (
              <>
                <Loader2 className="w-5 h-5 animate-spin mr-2" />
                <span>Extracting PDF Text &amp; Generating AI Evaluation...</span>
              </>
            ) : (
              <>
                <Sparkles className="w-5 h-5" />
                <span>Start AI Evaluation &amp; ATS Match</span>
              </>
            )}
          </button>

          <p className="text-center text-[11px] text-slate-500">
            Resumes are parsed securely with Apache PDFBox and evaluated via confidential AI prompts.
            No data is stored publicly.
          </p>
        </div>
      </form>
    </div>
  );
}
