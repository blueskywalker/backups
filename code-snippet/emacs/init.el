;
;; package manger
;;
;; repositories
;;


(when (>= emacs-major-version 24)
  (require 'package)
  (package-initialize)
;      (setq package-archives '(("gnu" . "http://elpa.gnu.org/packages/")
;			 ("marmalade" . "http://marmalade-repo.org/packages/")
;			 ("melpa" . "http://melpa.milkbox.net/packages/")))

  (add-to-list 'package-archives '("elpa" . "http://elpa.gnu.org/packages") t)
  (add-to-list 'package-archives '("marmalade" . "http://marmalade-repo.org/packages/") t)
  (add-to-list 'package-archives '("melpa" . "http://melpa.milkbox.net/packages/") t)
)



(add-to-list 'load-path "~/.emacs.d/el-get/el-get")

(unless (require 'el-get nil 'noerror)
  (with-current-buffer
      (url-retrieve-synchronously
       "https://raw.github.com/dimitri/el-get/master/el-get-install.el")
    (goto-char (point-max))
    (eval-print-last-sexp)
    )
  )

(add-to-list 'el-get-recipe-path "~/.emacs.d/el-get-user/recipes")
(el-get 'sync)



;;
;; add extras packages
;;
(add-to-list 'load-path "~/.emacs.d/site-packages")




;;
;; assign alt to meta
;;
(setq x-alt-keysym 'meta)


;;
;; add /usr/local/bin in path
;;
(setenv "PATH" (concat "/usr/local/bin:" (getenv "PATH")))
(setq exec-path (append exec-path '("/usr/local/bin")))

;;;; Navigate between windows using Alt-1, Alt-2, Shift-left, shift-up, shift-right
;;(windmove-default-keybindings)

;; Enable copy and pasting from clipboard
;;(setq x-select-enable-clipboard t)


;;
;; Ruby
;;(require 'cask "~/.cask/cask.el")
;;(cask-initialize)
;;(require 'pallet)
;;(add-to-list 'load-path "~/.emacs.d/site-packages/custom")

;;(load "00common-setup.el")
;;(load "01ruby.el")

;;
;;
;;(require 'auto-complete-config)
;;(add-to-list 'ac-dictionary-directories 
;;	     "~/.emacs.d/.cask/24.3.50.1/elpa/auto-complete-20130724.1750/dict")
;;(ac-config-default)
;;(setq ac-ignore-case nil)
;;(add-to-list 'ac-modes 'enh-ruby-mode)
;;(add-to-list 'ac-modes 'web-mode)


;;
;; auto indent
;(setq auto-indent-on-visit-filt t) ;; if you want auto-indent  on for files
;(require 'auto-indent-mode)

;;
;; auto completion
;;
(require 'auto-complete)
(global-auto-complete-mode t) ;; enable global-mode


(require 'auto-complete-config)
(add-to-list 'ac-dictionary-directories "~/.emacs.d/ac-dict")
(ac-config-default)


;;
;; company mode
;;
(require 'company)
;;(global-set-key "\t" 'company-complete-common)
(add-hook 'after-init-hook 'global-company-mode)

;;
;; pig mode
;;
(require 'pig-mode)

;;
;; python mode
;;
(require 'python-mode)
;;(add-hook 'python-mode-hook
;;	  (lambda ()	    
;;	    (pymacs-load "ropemacs" "rope-")))

;;
;; jedi
;;(add-hook 'python-mode-hook 'jedi:setup)
;;(setq jedi:setup-keys t)
;;(setq jedi:complete-on-dot t)

;;
;; pymacs
;;
(require 'pymacs)
(pymacs-load "ropemacs" "rope-")


;;
;; Tramp
;;
(require 'tramp)
(setq tramp-default-method "scp")

;;
;; scala-mode2
;;
(add-to-list 'load-path "~/.emacs.d/site-packages/scala-mode2")
(require 'scala-mode2)

;;
;; ensime
;;
(add-to-list 'load-path "~/.emacs.d/site-packages/ensime/elisp")
(require 'ensime)

;;
;; replace scala-mode-hook
;; 
(add-hook 'scala-mode-hook 'ensime-scala-hook)

;;
;; Runy Enhance mode
;(require 'ruby-mode)

(require 'enh-ruby-mode)
(setq ruby-indent-level 4)

;;
;;
;(require 'ruby-tools)

;;
;; 
;(add-hook 'ruby-mode-hook 'robe-mode)
;(add-hook 'ruby-mode-hook 'yard-mode)

(add-hook 'enh-ruby-mode-hook 'robe-mode)
(add-hook 'enh-ruby-mode-hook 'yard-mode)


;;
;; haskell
;;
(add-hook 'haskell-mode-hook 'turn-on-haskell-doc-mode)
(add-hook 'haskell-mode-hook 'turn-on-indentation)

;;
;; rsense
(setq rsense-home "/Users/jkim/Software/rsense-0.3")
(add-to-list 'load-path (concat rsense-home "/etc"))
(require 'rsense)
(add-hook 'ruby-mode-hook
          (lambda ()
            (add-to-list 'ac-sources 'ac-source-rsense-method)
            (add-to-list 'ac-sources 'ac-source-rsense-constant)))

;; 
;; find word on cursor
;;
;(add-hook 'prog-mode-hook (lambdda () (highlight-symbol-mode)))
;(setq highlight-symbol-mode-on-navigation-p t)
;(global-set-key [f3] 'highligt-symbol-next)
;(global-set-key [(shift f3)] 'highlight-symbol-prev)
