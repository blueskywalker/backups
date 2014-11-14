set nocompatible
filetype off

set rtp+=~/.vim/bundle/vundle/
call vundle#rc()

" Let Vundle Manage Vundle
" required!
Bundle 'gmarik/vundle' 

" My Bundles here:
"
" original repos on github"

Bundle 'tpope/vim-fugitive'
Bundle 'Lokaltog/vim-easymotion'
Bundle 'rstacruz/sparkup', {'rtp': 'vim/'}
Bundle 'flazz/vim-colorschemes'
Bundle 'ervandew/supertab'
Bundle 'MarcWeber/vim-addon-mw-utils'
Bundle 'reinh/vim-makegreen'
Bundle 'sjbach/lusty'
Bundle 'sontek/minibufexpl.vim'
Bundle 'tpope/vim-git'
Bundle 'sjl/gundo.vim'
Bundle 'fsouza/go.vim'
Bundle 'taglist.vim'
Bundle 'TaskList.vim'
Bundle 'VimPdb'
Bundle 'Vimpy'
Bundle 'Erlang-plugin-package'
Bundle 'derekwyatt/vim-scala'
Bundle 'xolox/vim-shell'
Bundle 'majutsushi/tagbar'

"vim-scripts repos

Bundle 'L9'
Bundle 'FuzzyFinder'
Bundle 'bash-support.vim'

"non github repos
Bundle 'git://git.wincent.com/command-t.git'
Bundle 'git://github.com/msanders/snipmate.vim.git'
"..."


"
" Brief help
" :BundleList              - list configured bundle
" :BundleInstall(!)        - install (update) bundle
" :BundleSearch(!) foo     - search(or refresh cache first) for foo
" :BundleClean(!)          - confirm(or auto-approve) removal of unused bundle
"
" see :h vundle for more details or wiki for FAQ
" NOTE: comments after Bundle command are not allowed..
"



"
" Pathogen
"
""let g:pahogen_disabled = [ 'pathogen','vundle' ]  "don't load self 
""call pathogen#infect()
""call pathogen#runtime_append_all_bundles()
""call pathogen#helptags()


"
" code folding
set foldmethod=manual
set foldlevel=3
set foldnestmax=4
"set foldmethod=syntax

" indentation
set autoindent
set tabstop=4 softtabstop=4 shiftwidth=4 expandtab

" visual 
highlight Normal ctermbg=black
set background=dark
"set cursorline
set t_Co=256

" Syntax 
syntax on
filetype on
filetype plugin on
filetype plugin indent on


" colorscheme
"colorscheme 256-grayvim 
"colorscheme kiss
colorscheme jellybeans 

" gundo
nnoremap <F5> :GundoToggle<CR>

"lusty
set hidden

"pep8
"let g:pep8_map='<leader>8'

"supertab
autocmd FileType python set omnifunc=pythoncomplete#Complete
let g:SuperTabDefaultCompletionType = "context"
set completeopt=menuone,longest,preview

"
" Window Movement
"

"map <c-j> <c-w>j
"map <c-k> <c-w>k
"map <c-l> <c-w>l
"map <c-h> <c-w>h

"
" Task lists
"
"map <leader>td <Plug>TaskList

"
" MakeGreen
"
"map <leader>] <Plug>MakeGreen

"
" Revision History
"
"map <leader>g :GundoToggle<CR>

"
" File Browser
"
"map <leader>n :NERDTreeToggle<CR>

"
" Refactoring and Go to definition
"
"map <leader>j :RopeGotoDefinition<CR>
"map <leader>r :RopeRename<CR>

"
" Searching
"
"nmap <leader>s <Esc>:Ack!


"
" Git
"
set statusline=%{fugitive#statusline()}

"
" Command T
"
"let g:CommandTMaxFiles=200000

"
" ConqueTerm
"
let g:ConqueTerm_Color = 1

"
" TagBar
"
nmap <F8> :TagbarToggle<CR>

