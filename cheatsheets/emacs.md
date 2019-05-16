## Some customisation changes I do every time I install emacs on a new PC

First, I download the custiomisations recommended for Clojure:  
https://www.braveclojure.com/basic-emacs/

Then, I remove all the Clojure-related settings :D. Well... if I'm not using Clojure.

Then I add / edit the following:

```
;; Start emacs maximized
(add-to-list 'default-frame-alist '(fullscreen . maximized))

;; Map markdown commands to pandoc... markdown doesn't work without this :/
(setq markdown-command "pandoc")

;; use ctrl-tab to switch between buffers
;; (global-set-key (kbd "<C-tab>") 'bury-buffer )

;; use ctrl-tab to go to the previous view
(global-set-key (kbd "<C-tab>") 'other-window )

;; use shift-arrows to switch between views
(windmove-default-keybindings)

;; increase font size for better readability
(set-face-attribute 'default nil :height 180)
```
